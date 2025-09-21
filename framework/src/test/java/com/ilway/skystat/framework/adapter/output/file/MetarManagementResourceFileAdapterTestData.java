package com.ilway.skystat.framework.adapter.output.file;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import static org.junit.jupiter.api.Assertions.*;

import com.ilway.skystat.framework.config.ResourceFileConfigData;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.ilway.skystat.domain.vo.metar.Metar;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class MetarManagementResourceFileAdapterTestData extends ResourceFileConfigData {

	private final int HOURS_OF_ONE_YEAR = 8_760;

	@Test
	@DisplayName("리소스파일에서 icao로 METAR 조회에 성공해야한다.")
	void findAllByIcaoTest() {
		String icao = "rksi";
		List<Metar> metarList = metarManagementUseCase.findAllByIcao(icao);
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
		List<Metar> metarList = metarManagementUseCase.findByIcaoAndPeriod(icao, retrievalPeriod);

		assertTrue(metarList.size() >= HOURS_OF_ONE_YEAR);
	}
}
