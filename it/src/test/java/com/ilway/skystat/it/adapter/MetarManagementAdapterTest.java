package com.ilway.skystat.it.adapter;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.management.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MetarManagementAdapterTest extends MySQLConfigData {

	private final MetarManagementResourceFileAdapter fileAdapter;
	private final MetarManagementMySQLAdapter sqlAdapter;

	@Autowired
	public MetarManagementAdapterTest(MetarManagementRepository repository,
	                                  EntityManager em) {
		super(repository, em);
		this.fileAdapter = new MetarManagementResourceFileAdapter();
		this.sqlAdapter = new MetarManagementMySQLAdapter(repository, em);
	}

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
