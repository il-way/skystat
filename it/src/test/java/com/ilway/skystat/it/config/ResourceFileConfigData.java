package com.ilway.skystat.it.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.MetarManagementInputPort;
import com.ilway.skystat.application.port.input.metar.scan.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WeatherStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WindRoseInputPort;
import com.ilway.skystat.application.port.output.MetarInventoryOutputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.MetarSaveFileUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.output.MetarParsingAdapter;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceFileConfigData {

	protected MetarManagementUseCase metarManagementUseCase;

	protected MetarSaveFileUseCase metarSaveFileUseCase;

	protected MetarManagementOutputPort metarManagementOutputPort;

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
		this.thresholdStatisticUseCase = new ThresholdStatisticInputPort(metarManagementOutputPort);
		this.weatherStatisticUseCase = new WeatherStatisticInputPort(metarManagementOutputPort);
		this.cloudStatisticUseCase = new CloudStatisticInputPort(metarManagementOutputPort);
		this.metarManagementUseCase = new MetarManagementInputPort(metarManagementOutputPort, metarParsingOutputPort);
		this.windRoseUseCase = new WindRoseInputPort(metarManagementOutputPort);
	}

}
