package com.ilway.skystat.it.adapter.output.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.*;
import com.ilway.skystat.application.port.input.internal.TemperatureStatisticAggregator;
import com.ilway.skystat.application.port.input.metar.scan.TemperatureStatisticInputPort;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarTemperatureQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.TemperatureStatisticQueryMySqlAdapter;
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

import java.math.RoundingMode;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Transactional
public class TemperatureStatisticQueryTest extends MySQLConfigData {

	private MetarTemperatureQueryRepository metarTemperatureQueryRepository;
	private TemperatureStatisticQueryMySqlAdapter adapter;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "RKSI";
	private RetrievalPeriod period = RetrievalPeriod.of(2019,5 );

	@Autowired
	public TemperatureStatisticQueryTest(MetarManagementRepository repository,
	                                     EntityManager em,
	                                     MetarTemperatureQueryRepository metarTemperatureQueryRepository) {
		super(repository, em);
		this.metarTemperatureQueryRepository = metarTemperatureQueryRepository;
		this.adapter = new TemperatureStatisticQueryMySqlAdapter(metarTemperatureQueryRepository);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndReportTimePeriod(icao, period);
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	@DisplayName("""
			집계대상 : 온도 집계 전반
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hourlyStatisticTest() {
		TemperatureStatisticQuery query = new TemperatureStatisticQuery(icao, period);

		TemperatureStatisticResult expected = temperatureStatisticUseCase.execute(query);

		List<HourlyTemperatureStatDto> hourlyQuery = adapter.findHourlyTemperatureStatistic(query.icao(), query.period());
		List<DailyTemperatureStatDto> dailyQuery = adapter.findDailyTemperatureStatistic(query.icao(), query.period());
		TemperatureStatisticResult actual = TemperatureStatisticAggregator.aggregate(hourlyQuery, dailyQuery, RoundingPolicy.of(2, HALF_UP));

		List<HourlyTemperatureStatDto> hourlyExpected = expected.hourly();
		List<MonthlyTemperatureStatDto> monthlyExpected = expected.monthly();
		List<YearlyTemperatureStatDto> yearlyExpected = expected.yearly();

		List<HourlyTemperatureStatDto> hourlyActual = actual.hourly();
		List<MonthlyTemperatureStatDto> monthlyActual = actual.monthly();
		List<YearlyTemperatureStatDto> yearlyActual = actual.yearly();

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual)),
			() -> assertFalse(monthlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertFalse(yearlyActual.isEmpty()),
			() -> assertEquals(yearlyExpected.size(), yearlyActual.size()),
			() -> assertTrue(yearlyExpected.containsAll(yearlyActual))
		);
	}

}
