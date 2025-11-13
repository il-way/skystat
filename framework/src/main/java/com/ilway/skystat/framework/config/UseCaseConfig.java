package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.input.metar.fallback.*;
import com.ilway.skystat.application.port.input.metar.query.*;
import com.ilway.skystat.application.port.input.metar.scan.*;
import com.ilway.skystat.application.port.output.*;
import com.ilway.skystat.application.usecase.*;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarBasicQueryRepository;
import com.ilway.skystat.framework.profile.Default;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Default
@Configuration
public class UseCaseConfig {

	@Bean
	public BasicStatisticUseCase basicStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                   MetarBasicStatisticQueryOutputPort metarBasicStatisticQueryOutputPort) {
		return new MetarBasicStatisticFallbackInputPort(
			new MetarBasicStatisticQueryInputPort(metarBasicStatisticQueryOutputPort),
			new MetarBasicStatisticInputPort(metarManagementOutputPort)
		);
	}

	@Bean
	public WindRoseUseCase windRoseUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                       WindRoseQueryOutputPort windRoseQueryOutputPort) {
		return new WindRoseFallbackInputPort(
			new WindRoseQueryInputPort(windRoseQueryOutputPort),
			new WindRoseInputPort(metarManagementOutputPort)
		);
	}

	@Bean
	public TemperatureStatisticUseCase temperatureStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                               TemperatureStatisticQueryOutputPort temperatureStatisticQueryOutputPort) {
		return new TemperatureStatisticFallbackInputPort(
			new TemperatureStatisticQueryInputPort(temperatureStatisticQueryOutputPort),
			new TemperatureStatisticInputPort(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                           ThresholdStatisticQueryOutputPort thresholdStatisticQueryOutputPort) {
		return new ThresholdStatisticFallbackInputPort(
			new ThresholdStatisticInputPort(metarManagementOutputPort),
			new ThresholdStatisticQueryInputPort(thresholdStatisticQueryOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                       WeatherStatisticQueryOutputPort weatherStatisticQueryOutputPort) {
		return new WeatherStatisticFallbackInputPort(
			new WeatherStatisticInputPort(metarManagementOutputPort),
			new WeatherStatisticQueryInputPort(weatherStatisticQueryOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                   CloudStatisticQueryOutputPort cloudStatisticQueryOutputPort) {
		return new CloudStatisticFallbackInputPort(
			new CloudStatisticInputPort(metarManagementOutputPort),
			new CloudStatisticQueryInputPort(cloudStatisticQueryOutputPort)
		);
	}

	@Bean
	public MetarInventoryUseCase metarInventoryUseCase(MetarInventoryOutputPort metarInventoryOutputPort) {
		return new MetarInventoryInputPort(metarInventoryOutputPort);
	}

	@Bean
	public MetarManagementUseCase metarManagementUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarManagementInputPort(metarManagementOutputPort, metarParsingOutputPort);
	}

	@Bean MetarSaveFileUseCase metarSaveFileUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarSaveFileInputPort(metarManagementOutputPort, metarParsingOutputPort);
	}

}
