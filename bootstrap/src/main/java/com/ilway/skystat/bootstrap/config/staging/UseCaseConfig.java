package com.ilway.skystat.bootstrap.config.staging;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.input.metar.fallback.CloudStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.fallback.TemperatureStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.fallback.ThresholdStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.fallback.WeatherStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.query.CloudStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.query.TemperatureStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.query.ThresholdStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.query.WeatherStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.scan.*;
import com.ilway.skystat.application.port.output.*;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.bootstrap.profile.Staging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Staging
@Configuration
public class UseCaseConfig {

	@Bean
	public TemperatureStatisticUseCase temperatureStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                               TemperatureStatisticQueryOutputPort temperatureStatisticQueryOutputPort) {
		return new TemperatureStatisticFallbackInputPort(
			new TemperatureStatisticInputPort(metarManagementOutputPort),
			new TemperatureStatisticQueryInputPort(temperatureStatisticQueryOutputPort)
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
	public WindRoseUseCase windRoseUseCase(MetarManagementOutputPort outputPort) {
		return new WindRoseInputPort(outputPort);
	}

	@Bean
	public MetarManagementUseCase metarManagementUseCase(MetarManagementOutputPort outputPort) {
		return new MetarManagementInputPort(outputPort);
	}

}
