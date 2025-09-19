//package com.ilway.skystat.framework.config;
//
//import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
//import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
//import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
//import com.ilway.skystat.application.port.input.CloudStatisticInputPort;
//import com.ilway.skystat.application.port.input.ThresholdStatisticInputPort;
//import com.ilway.skystat.application.port.input.WeatherStatisticInputPort;
//import com.ilway.skystat.application.port.input.WindRoseInputPort;
//import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
//import com.ilway.skystat.application.usecase.StatisticUseCase;
//import com.ilway.skystat.application.usecase.WindRoseUseCase;
//import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class UseCaseConfig {
//
//	@Bean
//	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
//		return new ThresholdStatisticInputPort(outputPort);
//	}
//
//	@Bean
//	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
//		return new WeatherStatisticInputPort(outputPort);
//	}
//
//	@Bean
//	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
//		return new CloudStatisticInputPort(outputPort);
//	}
//
//	@Bean
//	public WindRoseUseCase windRoseUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
//		return new WindRoseInputPort(outputPort);
//	}
//
//}
