package com.ilway.skystat.bootstrap.config.production;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.input.metar.fallback.*;
import com.ilway.skystat.application.port.input.metar.query.*;
import com.ilway.skystat.application.port.input.metar.scan.*;
import com.ilway.skystat.application.port.output.*;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.bootstrap.profile.Production;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Production
@Configuration
public class UseCaseConfig {

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
			new ThresholdStatisticQueryInputPort(thresholdStatisticQueryOutputPort),
			new ThresholdStatisticInputPort(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                       WeatherStatisticQueryOutputPort weatherStatisticQueryOutputPort) {
		return new WeatherStatisticFallbackInputPort(
			new WeatherStatisticQueryInputPort(weatherStatisticQueryOutputPort),
			new WeatherStatisticInputPort(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                   CloudStatisticQueryOutputPort cloudStatisticQueryOutputPort) {
		return new CloudStatisticFallbackInputPort(
			new CloudStatisticQueryInputPort(cloudStatisticQueryOutputPort),
			new CloudStatisticInputPort(metarManagementOutputPort)
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
	public MetarManagementUseCase metarManagementUseCase(MetarManagementOutputPort outputPort) {
		return new MetarManagementInputPort(outputPort);
	}
}
