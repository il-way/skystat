package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.port.output.*;
import com.ilway.skystat.framework.adapter.output.mysql.management.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.*;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.CloudStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.TemperatureStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.ThresholdStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.WeatherStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.windrose.WindRoseQueryMySqlAdapter;
import com.ilway.skystat.framework.profile.Default;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Default
@Configuration
public class OutputPortConfig {

	@Bean
	public MetarManagementOutputPort metarManagementOutputPort(MetarManagementRepository repo, EntityManager em) {
		return new MetarManagementMySQLAdapter(repo, em);
	}

	@Bean
	public CloudStatisticQueryOutputPort cloudStatisticQueryOutputPort(MetarCloudQueryRepository cloudQueryRepository) {
		return new CloudStatisticQueryMySqlAdapter(cloudQueryRepository);
	}

	@Bean
	public WeatherStatisticQueryOutputPort weatherStatisticQueryOutputPort(MetarWeatherQueryRepository weatherQueryRepository) {
		return new WeatherStatisticQueryMySqlAdapter(weatherQueryRepository);
	}

	@Bean
	public ThresholdStatisticQueryOutputPort thresholdStatisticQueryOutputPort(MetarMetricQueryRepository metricQueryRepository) {
		return new ThresholdStatisticQueryMySqlAdapter(metricQueryRepository);
	}

	@Bean
	public TemperatureStatisticQueryOutputPort temperatureStatisticQueryOutputPort(MetarTemperatureQueryRepository temperatureQueryRepository) {
		return new TemperatureStatisticQueryMySqlAdapter(temperatureQueryRepository);
	}

	@Bean
	public WindRoseQueryOutputPort windRoseQueryOutputPort(MetarWindRoseQueryRepository windRoseQueryRepository) {
		return new WindRoseQueryMySqlAdapter(windRoseQueryRepository);
	}

}
