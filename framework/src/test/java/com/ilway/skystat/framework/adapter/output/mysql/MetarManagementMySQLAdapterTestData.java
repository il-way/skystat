package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.*;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.MetarMySQLMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.metar.ReportType.METAR;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.HPA;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static com.ilway.skystat.domain.vo.weather.type.WeatherIntensity.MODERATE;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.BR;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MetarManagementMySQLAdapterTestData extends MySQLConfigData {

	@Autowired
	MetarManagementRepository repository;

	@Autowired
	EntityManager em;

	private static final String TEST_ICAO = "RKSI";

	public MetarManagementMySQLAdapterTestData(@Autowired MetarManagementRepository repository, @Autowired EntityManager em) {
		super(repository, em);
	}

	@Test
	@DisplayName("METAR 도메인 객체 1개 저장에 성공해야한다.")
	void saveTest() {
		Metar expected = new TestMetarData1().testData;
		metarManagementUseCase.save(expected);

		MetarData metarData = repository.findAll().getFirst();
		Metar actual = MetarMySQLMapper.metarDataToDomain(metarData);

		assertEquals(actual, expected);
	}

	@Test
	@DisplayName("METAR 도메인 여러 개 저장에 성공해야한다.")
	void saveAllTest() {
		List<Metar> expected = new ArrayList<>();
		expected.add(new TestMetarData1().testData);
		expected.add(new TestMetarData2().testData);

		metarManagementUseCase.saveAll(expected);

		List<Metar> actual = repository.findAll().stream()
			                   .map(MetarMySQLMapper::metarDataToDomain)
			                   .toList();

		assertAll(
			() -> assertEquals(expected.size(), actual.size()),
			() -> assertTrue(expected.containsAll(actual))
		);
	}

	@Test
	@DisplayName("icao를 통해 모든 METAR 데이터 조회에 성공해야한다.")
	void findAllByIcaoTest() {
		List<Metar> expected = new ArrayList<>();
		expected.add(new TestMetarData1().testData);
		expected.add(new TestMetarData2().testData);

		metarManagementUseCase.saveAll(expected);

		List<Metar> actual = metarManagementUseCase.findAllByIcao(TEST_ICAO);
		assertTrue(expected.containsAll(actual));
	}

	@Test
	@DisplayName("icao와 period를 통해 METAR 데이터 조회에 성공해야한다.")
	void findByIcaoAndPeriodTest() {
		List<Metar> expected = new ArrayList<>();
		expected.add(new TestMetarData1().testData);
		expected.add(new TestMetarData2().testData);

		metarManagementUseCase.saveAll(expected);

		ZonedDateTime from = expected.getFirst().getReportTime();
		ZonedDateTime to = expected.getLast().getReportTime().plusNanos(1);
		RetrievalPeriod period = new RetrievalPeriod(from, to);

		List<Metar> actual = metarManagementUseCase.findByIcaoAndPeriod(TEST_ICAO, period);

		assertTrue(expected.containsAll(actual));
	}

	public class TestMetarData1 {

		/*──────── 원문 (2024-01-01) ────────*/
		private final String rawText =
			"RKSI 010000Z 07002KT 3500 BR NSC 01/00 Q1029 NOSIG";

		/*──────── Metar 객체 ────────*/
		protected final Metar testData =
			Metar.builder()
				.rawText(rawText)
				.stationIcao("RKSI")
				.reportType(METAR)
				.observationTime(ofLenientUtc(2024, 1, 1, 0, 0))
				.wind(Wind.of(WindDirection.fixed(70), 2, 0, KT))
				.visibility (Visibility.of(3500, METERS))
				.temperature(Temperature.of(1,  CELSIUS))
				.dewPoint   (Temperature.of(0,  CELSIUS))
				.altimeter  (Altimeter.of(1029, HPA))
				.weathers(Weathers.of(List.of(
					Weather.of(MODERATE, null, List.of(BR))
				)))
				.clouds(Clouds.of(List.of()))   // NSC
				.remarks("NOSIG")
				.build();

	}

	public class TestMetarData2 {

		/*──────── 원문 (2024-01-01) ────────*/
		private final String rawText =
			"RKSI 010030Z 10004KT 030V150 4000 BR NSC 03/01 Q1030 NOSIG";

		/*──────── Metar 객체 ────────*/
		protected final Metar testData =
			Metar.builder()
				.rawText(rawText)
				.stationIcao("RKSI")
				.reportType(METAR)
				.observationTime(ofLenientUtc(2024, 1, 1, 0, 30))     // 010030Z
				.wind(Wind.of(WindDirection.fixed(100), 4, 0, KT))    // 100° 4 kt
				.visibility(Visibility.of(4000, METERS))              // 4 000 m
				.temperature(Temperature.of(3,  CELSIUS))             // 03 °C
				.dewPoint   (Temperature.of(1,  CELSIUS))             // 01 °C
				.altimeter  (Altimeter.of(1030, HPA))                 // Q1030
				.weathers(Weathers.of(List.of(
					Weather.of(MODERATE, null, List.of(BR))           // BR
				)))
				.clouds(Clouds.of(List.of()))                 // NSC
				.remarks("NOSIG")
				.build();

	}

}
