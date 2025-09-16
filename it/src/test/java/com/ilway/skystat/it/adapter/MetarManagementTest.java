package com.ilway.skystat.it.adapter;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
public class MetarManagementTest {

	@Autowired MetarManagementResourceFileAdapter fileAdapter;
	@Autowired MetarManagementMySQLAdapter sqlAdapter;

	private final static String ICAO = "rksi";

	@Test
	@DisplayName("리소스 파일에서 METAR raw text를 파싱해 MySQL로 저장하는데 성공해야한다.")
	void saveAllTest() {
		List<Metar> fromResource = fileAdapter.findAllByIcao(ICAO);
		sqlAdapter.saveAll(fromResource);

		List<Metar> saved = sqlAdapter.findAllByIcao(ICAO);

		int expectedSize = fromResource.size();
		RetrievalPeriod expectedPeriod = new RetrievalPeriod(
			fromResource.getFirst().getReportTime(),
			fromResource.getLast().getReportTime()
		);

		int actualSize = saved.size();
		RetrievalPeriod actualPeriod = new RetrievalPeriod(
			saved.getFirst().getReportTime(),
			saved.getLast().getReportTime()
		);

		assertAll(
			() -> assertEquals(expectedSize, actualSize),
			() -> assertEquals(expectedPeriod, actualPeriod)
		);
	}

}
