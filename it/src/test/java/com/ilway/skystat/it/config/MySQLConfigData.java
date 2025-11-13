package com.ilway.skystat.it.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.MetarManagementInputPort;
import com.ilway.skystat.application.port.input.metar.MetarSaveFileInputPort;
import com.ilway.skystat.application.port.input.metar.scan.*;
import com.ilway.skystat.application.port.output.MetarInventoryOutputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.application.usecase.*;
import com.ilway.skystat.framework.adapter.output.MetarParsingAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.inventory.MetarInventoryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.management.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import jakarta.persistence.EntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MySQLConfigData {

	protected MetarManagementUseCase metarManagementUseCase;

	protected MetarSaveFileUseCase metarSaveFileUseCase;

	protected MetarManagementOutputPort metarManagementOutputPort;

	protected MetarInventoryOutputPort metarInventoryOutputPort;

	protected MetarParsingOutputPort metarParsingOutputPort;

	protected StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase;

	protected StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase;

	protected StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase;

	protected TemperatureStatisticUseCase temperatureStatisticUseCase;

	protected WindRoseUseCase windRoseUseCase;

	protected Resource resource;

	public MySQLConfigData(MetarManagementRepository repository,
	                       EntityManager em,
	                       MetarInventoryRepository metarInventoryRepository) {
		this.metarManagementOutputPort = new MetarManagementMySQLAdapter(repository, em);
		this.metarInventoryOutputPort = new MetarInventoryMySqlAdapter(metarInventoryRepository);
		this.metarParsingOutputPort = new MetarParsingAdapter();
		this.metarManagementUseCase = new MetarManagementInputPort(metarManagementOutputPort, metarParsingOutputPort);
		this.metarSaveFileUseCase = new MetarSaveFileInputPort(metarManagementOutputPort, metarParsingOutputPort);
		this.thresholdStatisticUseCase = new ThresholdStatisticInputPort(metarManagementOutputPort);
		this.weatherStatisticUseCase = new WeatherStatisticInputPort(metarManagementOutputPort);
		this.cloudStatisticUseCase = new CloudStatisticInputPort(metarManagementOutputPort);
		this.temperatureStatisticUseCase = new TemperatureStatisticInputPort(metarManagementOutputPort);
		this.windRoseUseCase = new WindRoseInputPort(metarManagementOutputPort);
		this.resource = new ClassPathResource("/data/metar/");
	}

}
