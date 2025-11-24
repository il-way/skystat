package com.ilway.skystat.it.adapter.output.windrose;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.windrose.*;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWindRoseQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.windrose.WindRoseQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@Transactional
public class WindRoseQueryMySqlAdapterTest extends MySQLConfigData {

	private MetarWindRoseQueryRepository windRoseQueryRepository;
	private WindRoseQueryMySqlAdapter adapter;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "KJFK";
	private RetrievalPeriod period = new RetrievalPeriod(
		ofLenientUtc(2019, 1, 1, 0, 0),
		ofLenientUtc(2024, 1, 1, 0, 0)
	);

	@Autowired
	public WindRoseQueryMySqlAdapterTest(MetarManagementRepository repository,
	                                     EntityManager em,
	                                     MetarInventoryRepository metarInventoryRepository,
	                                     MetarWindRoseQueryRepository windRoseQueryRepository) {

		super(repository, em, metarInventoryRepository);
		this.windRoseQueryRepository = windRoseQueryRepository;
		this.adapter = new WindRoseQueryMySqlAdapter(windRoseQueryRepository);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndReportTimePeriod(icao, period);
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	@DisplayName("""
			집계대상 : 월별 바람장미
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void generateDefaultTest() {
		WindRoseResult expected = windRoseUseCase.generateDefault(icao, period);

		List<MonthlyWindRoseRow> rows = adapter.aggregateDefaultByMonth(icao, period);
		List<MonthlyCountDto> variables = adapter.countVariableByMonth(icao, period);
		WindRoseResult actual = toWindRoseResult(rows, variables);

		assertAll(
			() -> assertEquals(expected.totalCount(), actual.totalCount()),
			() -> assertEquals(expected.variableSize(), actual.variableSize()),
			() -> assertEquals(expected.sampleSize(), actual.sampleSize()),
			() -> assertEquals(expected.windRoseMap(), actual.windRoseMap())
		);
	}

	private WindRoseResult toWindRoseResult(List<MonthlyWindRoseRow> rows, List<MonthlyCountDto> variables) {
		int sampleSize = rows.stream().map(MonthlyWindRoseRow::freq)
			                 .mapToInt(Integer::intValue)
			                 .sum();

		int missingCount = variables.stream()
			                   .map(MonthlyCountDto::count)
			                   .mapToInt(Long::intValue)
			                   .sum();


		Map<Month, WindRose> windRoseMap = rows.stream().collect(groupingBy(
			r -> Month.of(r.month()),
			collectingAndThen(
				toList(),
				list -> toWindRose(list, SpeedBin.of5KtSpeedBins(), DirectionBin.of16DirectionBins())
			)
		));

		return new WindRoseResult(
			sampleSize + missingCount,
			sampleSize,
			missingCount,
			windRoseMap
		);
	}

	private WindRose toWindRose(List<MonthlyWindRoseRow> rows, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		LinkedHashMap<WindRose.BinPair, Long> freqMap = WindRose.initFrequencyMap(speedBins, directionBins);
		for (MonthlyWindRoseRow r : rows) {
			SpeedBin sb = speedBins.get(r.speedOrder());
			DirectionBin db = directionBins.get(r.dirOrder());
			WindRose.BinPair key = new WindRose.BinPair(sb, db);
			freqMap.put(key, freqMap.get(key) + r.freq());
		}

		long sampleSize = rows.stream().mapToLong(MonthlyWindRoseRow::freq).sum();
		return new WindRose(speedBins, directionBins, freqMap, sampleSize);
	}

}
