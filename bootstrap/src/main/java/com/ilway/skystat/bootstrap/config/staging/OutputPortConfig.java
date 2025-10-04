package com.ilway.skystat.bootstrap.config.staging;

import com.ilway.skystat.application.port.output.CloudStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;
import com.ilway.skystat.bootstrap.profile.Production;
import com.ilway.skystat.bootstrap.profile.Staging;
import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarCloudQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarMetricQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWeatherQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.CloudStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.ThresholdStatisticQueryMySqlAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.statistic.WeatherStatisticQueryMySqlAdapter;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Staging
@Configuration
public class OutputPortConfig {

	@Bean
	public MetarManagementOutputPort metarManagementOutputPort(
		MetarManagementRepository repo,
		EntityManager em) {
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


}
