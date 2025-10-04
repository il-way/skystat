package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.input.metar.fallback.CloudStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.fallback.ThresholdStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.fallback.WeatherStatisticFallbackInputPort;
import com.ilway.skystat.application.port.input.metar.query.CloudStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.query.ThresholdStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.query.WeatherStatisticQueryInputPort;
import com.ilway.skystat.application.port.input.metar.scan.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WeatherStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WindRoseInputPort;
import com.ilway.skystat.application.port.output.CloudStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.profile.Default;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Default
@Configuration
public class UseCaseConfig {

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
