package config;

import dto.statistic.CloudStatisticQuery;
import dto.statistic.ThresholdStatisticQuery;
import dto.statistic.WeatherStatisticQuery;
import dto.taf.CloudConditionQuery;
import dto.taf.ThresholdConditionQuery;
import dto.taf.WeatherConditionQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import port.input.*;
import port.output.MetarManagementOutputPort;
import port.output.TafManagementOutputPort;
import usecase.ConditionUseCase;
import usecase.StatisticUseCase;
import usecase.WindRoseUseCase;

@Configuration
public class UseCaseConfig {

	@Bean
	ConditionUseCase<CloudConditionQuery> cloudConditionUseCase(TafManagementOutputPort outputPort) {
		return new CloudConditionInputPort(outputPort);
	}

	@Bean
	ConditionUseCase<WeatherConditionQuery> weatherConditionUseCase(TafManagementOutputPort outputPort) {
		return new WeatherConditionInputPort(outputPort);
	}

	@Bean
	ConditionUseCase<ThresholdConditionQuery> thresholdConditionUseCase(TafManagementOutputPort outputPort) {
		return new ThresholdConditionInputPort(outputPort);
	}

	@Bean
	StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new ThresholdStatisticInputPort(outputPort);
	}

	@Bean
	StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new WeatherStatisticInputPort(outputPort);
	}

	@Bean
	StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort outputPort) {
		return new CloudStatisticInputPort(outputPort);
	}

	@Bean
	WindRoseUseCase windRoseUseCase(MetarManagementOutputPort outputPort) {
		return new WindRoseInputPort(outputPort);
	}

}
