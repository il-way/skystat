package com.ilway.skystat.it.adapter.output.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.model.weather.WeatherConditionPredicate;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.domain.service.WeatherOperation;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.WeatherPhenomenonData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWeatherQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.WeatherStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator.peelOffZeroCount;
import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.TS;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.VC;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class WeatherStatisticQueryTest extends MySQLConfigData {

	private MetarWeatherQueryRepository metarWeatherQueryRepository;
	private WeatherStatisticQueryMySqlAdapter adapter;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "RKSI";
	private RetrievalPeriod period = new RetrievalPeriod(
		ofLenientUtc(2019, 1, 1, 0, 0),
		ofLenientUtc(2024, 1, 1, 0, 0)
	);

	@Autowired
	public WeatherStatisticQueryTest(MetarManagementRepository repository,
	                                 EntityManager em,
	                                 MetarWeatherQueryRepository metarWeatherQueryRepository) {
		super(repository, em);
		this.metarWeatherQueryRepository = metarWeatherQueryRepository;
		this.adapter = new WeatherStatisticQueryMySqlAdapter(metarWeatherQueryRepository);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndPeriod(icao, period);
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	@DisplayName("""
			집계대상 : 단일 Phenomena
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasPhenomenonTest() {
		for (WeatherPhenomenon wp : WeatherPhenomenon.values()) {
			WeatherCondition condition = new WeatherCondition(WeatherConditionPredicate.HAS_PHENOMENA, List.of(wp));
			WeatherStatisticQuery query = new WeatherStatisticQuery(icao, period, condition);

			ObservationStatisticResult execute = weatherStatisticUseCase.execute(query);
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

	@Test
	@DisplayName("""
			집계대상 : 단일 Descriptor
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasDescriptorTest() {
		for (WeatherDescriptor wd : WeatherDescriptor.values()) {
			WeatherCondition condition = new WeatherCondition(WeatherConditionPredicate.HAS_DESCRIPTORS, List.of(wd));
			WeatherStatisticQuery query = new WeatherStatisticQuery(icao, period, condition);

			ObservationStatisticResult execute = weatherStatisticUseCase.execute(query);
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

	@Test
	@DisplayName("""
			집계대상 : 복합 Phenomena (SNRA, RASN, ...)
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasPhenomenaTest() {
		WeatherCondition condition = new WeatherCondition(WeatherConditionPredicate.HAS_PHENOMENA, List.of(SN, RA));
		WeatherStatisticQuery query = new WeatherStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = weatherStatisticUseCase.execute(query);
		ObservationStatisticResult expected = peelOffZeroCount(execute);
		List<MonthlyCountDto> monthlyExpected = expected.monthly();
		List<HourlyCountDto> hourlyExpected = expected.hourly();

		List<MonthlyCountDto> monthlyActual = adapter.countDistinctDaysByMonth(icao, period, condition);
		List<HourlyCountDto> hourlyActual = adapter.countDistinctHoursByMonth(icao, period, condition);

		List<HourlyCountDto> diff = getDiff(hourlyExpected, hourlyActual);
		log.info("# missing > {}", diff);

		assertAll(
			() -> assertEquals(monthlyExpected.size(), monthlyActual.size()),
			() -> assertTrue(monthlyExpected.containsAll(monthlyActual)),
			() -> assertEquals(hourlyExpected.size(), hourlyActual.size()),
			() -> assertTrue(hourlyExpected.containsAll(hourlyActual))
		);
	}

	@Test
	@DisplayName("""
			집계대상 : 복합 Descriptors (VCTS, ...)
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasDescriptorsTest() {
		WeatherCondition condition = new WeatherCondition(WeatherConditionPredicate.HAS_DESCRIPTORS, List.of(VC, TS));
		WeatherStatisticQuery query = new WeatherStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = weatherStatisticUseCase.execute(query);
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
			집계대상 : 복합 기상 (VCTSRA, ...)
			요구사항 : DB Query를 이용한 집계와 METAR 풀스캔을 통한 집계 결과가 같아야 한다
			예상결과 : 성공
		""")
	void hasDescriptorsAndPhenomenaTest() {
		WeatherCondition condition = new WeatherCondition(WeatherConditionPredicate.HAS_DESCRIPTORS_AND_PHENOMENA, List.of(VC, TS, RA));
		WeatherStatisticQuery query = new WeatherStatisticQuery(icao, period, condition);

		ObservationStatisticResult execute = weatherStatisticUseCase.execute(query);
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

	private <T> List<T> getDiff(List<T> expect, List<T> actual) {
		List<T> result = new ArrayList<>();
		if (actual.containsAll(expect)) {
			for (T el : actual) {
				if (!expect.contains(el)) result.add(el);
			}
			return result;
		}
		else {
			for (T el : expect) {
				if (!actual.contains(el)) result.add(el);
			}
			return result;
		}
	}

}
