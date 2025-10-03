package com.ilway.skystat.it.adapter.output.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.taf.CloudConditionQuery;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.model.weather.CloudConditionPredicate;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarCloudQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWeatherQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.CloudStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.WeatherStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ilway.skystat.application.model.weather.CloudConditionPredicate.HAS_CLOUDTYPE;
import static com.ilway.skystat.application.model.weather.CloudConditionPredicate.HAS_COVERAGE;
import static com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator.*;
import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.BKN;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.CB;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class CloudStatisticQueryTest extends MySQLConfigData {

	private MetarCloudQueryRepository metarCloudQueryRepository;
	private CloudStatisticQueryMySqlAdapter adapter;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "RKSI";
	private RetrievalPeriod period = new RetrievalPeriod(
		ofLenientUtc(2019, 1, 1, 0, 0),
		ofLenientUtc(2024, 1, 1, 0, 0)
	);

	@Autowired
	public CloudStatisticQueryTest(MetarManagementRepository repository,
	                               EntityManager em,
	                               MetarCloudQueryRepository metarCloudQueryRepository) {
		super(repository, em);
		this.metarCloudQueryRepository = metarCloudQueryRepository;
		this.adapter = new CloudStatisticQueryMySqlAdapter(metarCloudQueryRepository);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndPeriod(icao, period);
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	@DisplayName("""
			집계대상 : 단일 CloudType
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasCloudTypeTest() {
		CloudCondition condition = new CloudCondition(HAS_CLOUDTYPE, CB);
		CloudStatisticQuery query = new CloudStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = cloudStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);
		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("""
			집계대상 : 단일 CloudCoverage
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasCloudCoverageTest() {
		CloudCondition condition = new CloudCondition(HAS_COVERAGE, BKN);
		CloudStatisticQuery query = new CloudStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = cloudStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);
		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

}
