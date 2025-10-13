package com.ilway.skystat.it.adapter.output.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.model.weather.ThresholdCondition;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.unit.PressureUnit;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarMetricQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.ThresholdStatisticQueryMySqlAdapter;
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

import java.util.List;

import static com.ilway.skystat.application.model.generic.Comparison.GTE;
import static com.ilway.skystat.application.model.generic.Comparison.LTE;
import static com.ilway.skystat.application.model.weather.MetricField.*;
import static com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator.peelOffZeroCount;
import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.HPA;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class ThresholdStatisticQueryTest extends MySQLConfigData {

	private MetarMetricQueryRepository metarMetricQueryRepository;
	private ThresholdStatisticQueryMySqlAdapter adapter;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "RKSI";
	private RetrievalPeriod period = new RetrievalPeriod(
		ofLenientUtc(2019, 1, 1, 0, 0),
		ofLenientUtc(2024, 1, 1, 0, 0)
	);

	@Autowired
	public ThresholdStatisticQueryTest(MetarManagementRepository repository,
	                                   EntityManager em,
	                                   MetarInventoryRepository metarInventoryRepository,
	                                   MetarMetricQueryRepository metarMetricQueryRepository) {

		super(repository, em, metarInventoryRepository);
		this.metarMetricQueryRepository = metarMetricQueryRepository;
		this.adapter = new ThresholdStatisticQueryMySqlAdapter(metarMetricQueryRepository);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndReportTimePeriod(icao, period);
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	@DisplayName("시정 OOOm 이하 조회에 성공해야 한다.")
	void visibilityLteTest() {
		ThresholdCondition condition = new ThresholdCondition(VISIBILITY, LTE, 1600, METERS);
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);

		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("최대풍속 OO0KT 이하 조회에 성공해야 한다.")
	void windPeakGteTest() {
		ThresholdCondition condition = new ThresholdCondition(WIND_PEAK, GTE, 35, KT);
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);

		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("풍속 O0OKT 이하 조회에 성공해야 한다.")
	void windSpeedGteTest() {
		ThresholdCondition condition = new ThresholdCondition(WIND_SPEED, GTE, 15, KT);
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);

		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("운저 OO0FT 이하 조회에 성공해야 한다.")
	void ceilingLteTest() {
		ThresholdCondition condition = new ThresholdCondition(LOWEST_CEILING, LTE, 500, FEET);
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);

		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("운저 OO0FT 이하 조회에 성공해야 한다.")
	void altimeterLteTest() {
		ThresholdCondition condition = new ThresholdCondition(ALTIMETER, LTE, 996, HPA);
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);

		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		assertAll(
			() -> assertFalse(hourlyActual.isEmpty()),
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

}
