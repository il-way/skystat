package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.WeatherStatisticInputPort;
import com.ilway.skystat.application.port.input.WindRoseInputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@TestConfiguration
public class MySQLConfig {

	@Bean
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
		return new ThresholdStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
		return new WeatherStatisticInputPort(outputPort);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
		return new CloudStatisticInputPort(outputPort);
	}

	@Bean
	public WindRoseUseCase windRoseUseCase(@Qualifier("metarManagementMySQLAdapter") MetarManagementOutputPort outputPort) {
		return new WindRoseInputPort(outputPort);
	}

	@Bean
	public Resource metarResourceDir() {
		return new ClassPathResource("/data/metar/");
	}


}
