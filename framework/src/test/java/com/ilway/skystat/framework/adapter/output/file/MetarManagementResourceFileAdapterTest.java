package com.ilway.skystat.framework.adapter.output.file;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import static org.junit.jupiter.api.Assertions.*;

import com.ilway.skystat.framework.FrameworkTestApp;
import com.ilway.skystat.framework.adapter.output.mysql.MySQLConfig;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootTest(classes = { ResourceFileConfig.class })
@Transactional
@RequiredArgsConstructor
public class MetarManagementResourceFileAdapterTest {

	@Autowired MetarManagementResourceFileAdapter adapter;

	private final int HOURS_OF_ONE_YEAR = 8_760;

	@Test
	@DisplayName("리소스파일에서 icao로 METAR 조회에 성공해야한다.")
	void findAllByIcaoTest() {
		String icao = "rksi";
		List<Metar> metarList = adapter.findAllByIcao(icao);
		ZonedDateTime observationTime = metarList.getFirst()
			                                .getObservationTime();

		System.out.println(observationTime);
		assertTrue(metarList.size() > HOURS_OF_ONE_YEAR);
	}

	@Test
	@DisplayName("리소스파일에서 icao와 period로 METAR 조회에 성공해야한다.")
	void findByIcaoAndPeriodTest() {
		String icao = "rksi";
		RetrievalPeriod retrievalPeriod = new RetrievalPeriod(
			ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2023, 12, 31, 23, 59, 0, 0, ZoneOffset.UTC)
		);
		List<Metar> metarList = adapter.findByIcaoAndPeriod(icao, retrievalPeriod);

		assertTrue(metarList.size() >= HOURS_OF_ONE_YEAR);
	}
}
