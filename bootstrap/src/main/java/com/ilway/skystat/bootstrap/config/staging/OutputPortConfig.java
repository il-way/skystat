package com.ilway.skystat.bootstrap.config.staging;

import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.bootstrap.profile.Production;
import com.ilway.skystat.bootstrap.profile.Staging;
import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
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


}
