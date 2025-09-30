package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.input.metar.scan.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WeatherStatisticInputPort;
import com.ilway.skystat.application.port.input.metar.scan.WindRoseInputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
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
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new ThresholdStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new WeatherStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new CloudStatisticInputPort(outputPort);
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
