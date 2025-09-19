package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.FrameworkTestApp;
import com.ilway.skystat.framework.adapter.output.data.metar.MetarTestData;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.MetarMySQLMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { MySQLConfig.class })
@Transactional
@ActiveProfiles("test")
public class MetarManagementMySQLAdapterTest extends MetarTestData {

	@Autowired
	private MetarManagementMySQLAdapter adapter;

	@Autowired
	private MetarManagementRepository repository;

	private static final String TEST_ICAO = "RKSI";

	@Test
	@DisplayName("METAR 도메인 객체 1개 저장에 성공해야한다.")
	void saveTest() {
		List<Metar> metars = metarListMap.get(TEST_ICAO);
		Metar expected = metars.getFirst();

		adapter.save(expected);

		MetarData metarData = repository.findAll().getFirst();
		Metar actual = MetarMySQLMapper.metarDataToDomain(metarData);

		assertEquals(actual, expected);
	}

	@Test
	@DisplayName("METAR 도메인 여러 개 저장에 성공해야한다.")
	void saveAllTest() {
		List<Metar> expected = metarListMap.get(TEST_ICAO);
		adapter.saveAll(expected);

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
		List<Metar> expected = metarListMap.get(TEST_ICAO);
		adapter.saveAll(expected);

		List<Metar> actual = adapter.findAllByIcao(TEST_ICAO);
		assertTrue(expected.containsAll(actual));
	}

	@Test
	@DisplayName("icao와 period를 통해 METAR 데이터 조회에 성공해야한다.")
	void findByIcaoAndPeriodTest() {
		List<Metar> expected = metarListMap.get(TEST_ICAO);
		adapter.saveAll(expected);

		ZonedDateTime from = expected.getFirst().getReportTime();
		ZonedDateTime to = expected.getLast().getReportTime();
		RetrievalPeriod period = new RetrievalPeriod(from, to);

		List<Metar> actual = adapter.findByIcaoAndPeriod(TEST_ICAO, period);

		assertTrue(expected.containsAll(actual));
	}

}
