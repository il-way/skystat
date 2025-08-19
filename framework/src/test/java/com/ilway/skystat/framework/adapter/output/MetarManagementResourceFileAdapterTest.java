package com.ilway.skystat.framework.adapter.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class MetarManagementResourceFileAdapterTest {

	private MetarManagementOutputPort adapter = new MetarManagementResourceFileAdapter(
		new ClassPathResource("/data/metar/")
	);

	private final int HOURS_OF_YEAR = 8_760;

	@Test
	void 리소스파일에서_icao로_메타조회에_성공해야한다() {
		String icao = "rksi";
		List<Metar> metarList = adapter.findAllByIcao(icao);
		assertTrue(metarList.size() > 1 * HOURS_OF_YEAR);
	}

	@Test
	void 리소스파일에서_icao_및_기간별_메타조회에_성공해야한다() {
		String icao = "rksi";
		RetrievalPeriod retrievalPeriod = new RetrievalPeriod(
			ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2023, 12, 31, 23, 59, 0, 0, ZoneOffset.UTC)
		);
		List<Metar> metarList = adapter.findByIcaoAndPeriod(icao, retrievalPeriod);

		assertTrue(metarList.size() >= HOURS_OF_YEAR);
	}
}
