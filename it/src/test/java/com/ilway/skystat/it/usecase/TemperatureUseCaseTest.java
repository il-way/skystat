package com.ilway.skystat.it.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.*;
import com.ilway.skystat.application.port.input.metar.scan.TemperatureStatisticInputPort;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;

@Slf4j
@SpringBootTest
@Transactional
public class TemperatureUseCaseTest extends MySQLConfigData {

	private TemperatureStatisticInputPort temperatureStatisticInputPort;
	private MetarManagementResourceFileAdapter fileAdapter;
	private String icao = "RKSI";

	@Autowired
	public TemperatureUseCaseTest(MetarManagementRepository repository,
	                              EntityManager em,
	                              MetarInventoryRepository metarInventoryRepository) {

		super(repository, em, metarInventoryRepository);
		temperatureStatisticInputPort = new TemperatureStatisticInputPort(metarManagementOutputPort);
		fileAdapter = new MetarManagementResourceFileAdapter();
	}

	@BeforeEach
	void init() {
		List<Metar> metars = fileAdapter.findByIcaoAndReportTimePeriod(icao, RetrievalPeriod.of(2019, 5));
		metarManagementUseCase.saveAll(metars);
	}

	@Test
	void test() {
		TemperatureStatisticQuery query = new TemperatureStatisticQuery(icao, RetrievalPeriod.of(2019, 1));
		TemperatureStatisticResult execute = temperatureStatisticInputPort.execute(query);

		List<MonthlyTemperatureStatDto> monthly = execute.monthly();
		List<HourlyTemperatureStatDto> hourly = execute.hourly();
		List<YearlyTemperatureStatDto> yearly = execute.yearly();

		log.info("# 2019 > {}", yearly);
		log.info("# 2019 monthly > {}", monthly);
		log.info("# 2019 hourly > {}", hourly);
	}

}
