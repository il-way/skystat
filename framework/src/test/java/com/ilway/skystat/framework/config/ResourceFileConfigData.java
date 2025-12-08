package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.service.metar.scan.CloudStatisticService;
import com.ilway.skystat.application.service.metar.scan.ThresholdStatisticService;
import com.ilway.skystat.application.service.metar.scan.WeatherStatisticService;
import com.ilway.skystat.application.service.metar.scan.WindRoseService;
import com.ilway.skystat.application.port.output.MetarInventoryOutputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.application.service.metar.MetarManagementService;
import com.ilway.skystat.application.port.input.MetarManagementUseCase;
import com.ilway.skystat.application.port.input.MetarSaveFileUseCase;
import com.ilway.skystat.application.port.input.StatisticUseCase;
import com.ilway.skystat.application.port.input.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.output.MetarParsingAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceFileConfigData {

	protected MetarManagementUseCase metarManagementUseCase;

	protected MetarManagementOutputPort metarManagementOutputPort;

	protected MetarSaveFileUseCase metarSaveFileUseCase;

	protected MetarInventoryOutputPort metarInventoryOutputPort;

	protected MetarParsingOutputPort metarParsingOutputPort;

	protected StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase;

	protected StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase;

	protected StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase;

	protected WindRoseUseCase windRoseUseCase;

	protected Resource resource;

	public ResourceFileConfigData() {
		this.resource = new ClassPathResource("/data/metar/");
		this.metarManagementOutputPort = new MetarManagementResourceFileAdapter();
		this.metarParsingOutputPort = new MetarParsingAdapter();
		this.thresholdStatisticUseCase = new ThresholdStatisticService(metarManagementOutputPort);
		this.weatherStatisticUseCase = new WeatherStatisticService(metarManagementOutputPort);
		this.cloudStatisticUseCase = new CloudStatisticService(metarManagementOutputPort);
		this.metarManagementUseCase = new MetarManagementService(metarManagementOutputPort, metarParsingOutputPort);
		this.windRoseUseCase = new WindRoseService(metarManagementOutputPort);
	}

}
