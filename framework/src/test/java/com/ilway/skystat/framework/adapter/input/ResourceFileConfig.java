package com.ilway.skystat.framework.adapter.input;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.metar.*;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@TestConfiguration
public class ResourceFileConfig {

	@Bean
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase() {
		return new ThresholdStatisticInputPort(metarManagementOutputPort());
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase() {
		return new WeatherStatisticInputPort(metarManagementOutputPort());
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase() {
		return new CloudStatisticInputPort(metarManagementOutputPort());
	}

	@Bean
	public WindRoseUseCase windRoseUseCase() {

		return new WindRoseInputPort(metarManagementOutputPort());
	}

	@Bean
	public MetarManagementUseCase metarManagementUseCase() {
		return new MetarManagementInputPort(metarManagementOutputPort());
	}

	@Bean
	public MetarManagementOutputPort metarManagementOutputPort() {
		return new MetarManagementResourceFileAdapter(metarResourceDir());
	}

	@Bean
	public Resource metarResourceDir() {
		return new ClassPathResource("/data/metar/");
	}


}
