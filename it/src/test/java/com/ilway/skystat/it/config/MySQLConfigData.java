package com.ilway.skystat.it.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.MetarManagementInputPort;
import com.ilway.skystat.application.port.input.metar.scan.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WeatherStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WindRoseInputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import jakarta.persistence.EntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MySQLConfigData {

	protected MetarManagementUseCase metarManagementUseCase;

	protected MetarManagementOutputPort metarManagementOutputPort;

	protected StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase;

	protected StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase;

	protected StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase;

	protected WindRoseUseCase windRoseUseCase;

	protected Resource resource;

	public MySQLConfigData(MetarManagementRepository repository, EntityManager em) {
		this.metarManagementOutputPort = new MetarManagementMySQLAdapter(repository, em);
		this.metarManagementUseCase = new MetarManagementInputPort(metarManagementOutputPort);
		this.thresholdStatisticUseCase = new ThresholdStatisticInputPort(metarManagementOutputPort);
		this.weatherStatisticUseCase = new WeatherStatisticInputPort(metarManagementOutputPort);
		this.cloudStatisticUseCase = new CloudStatisticInputPort(metarManagementOutputPort);
		this.windRoseUseCase = new WindRoseInputPort(metarManagementOutputPort);
		this.resource = new ClassPathResource("/data/metar/");
	}

}
