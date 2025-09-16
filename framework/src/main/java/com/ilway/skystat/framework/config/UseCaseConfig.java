package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.dto.taf.CloudConditionQuery;
import com.ilway.skystat.application.dto.taf.ThresholdConditionQuery;
import com.ilway.skystat.application.dto.taf.WeatherConditionQuery;
import com.ilway.skystat.application.port.input.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.TafManagementOutputPort;
import com.ilway.skystat.application.usecase.ConditionUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class UseCaseConfig {

//	@Bean
//	public ConditionUseCase<CloudConditionQuery> cloudConditionUseCase(TafManagementOutputPort outputPort) {
//		return new CloudConditionInputPort(outputPort);
//	}
//
//	@Bean
//	public ConditionUseCase<WeatherConditionQuery> weatherConditionUseCase(TafManagementOutputPort outputPort) {
//		return new WeatherConditionInputPort(outputPort);
//	}
//
//	@Bean
//	public ConditionUseCase<ThresholdConditionQuery> thresholdConditionUseCase(TafManagementOutputPort outputPort) {
//		return new ThresholdConditionInputPort(outputPort);
//	}

	@Bean
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(@Qualifier("metarManagementResourceFileAdapter") MetarManagementOutputPort outputPort) {
		return new ThresholdStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(@Qualifier("metarManagementResourceFileAdapter") MetarManagementOutputPort outputPort) {
		return new WeatherStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(@Qualifier("metarManagementResourceFileAdapter") MetarManagementOutputPort outputPort) {
		return new CloudStatisticInputPort(outputPort);
	}

	@Bean
	public WindRoseUseCase windRoseUseCase(@Qualifier("metarManagementResourceFileAdapter") MetarManagementOutputPort outputPort) {
		return new WindRoseInputPort(outputPort);
	}

	@Bean
	public Resource metarResourceDir() {
		return new ClassPathResource("/data/metar/");
	}

}
