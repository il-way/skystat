package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.MetarManagementMySQLAdapter;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.profile.Default;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Default
@Configuration
public class OutputPortConfig {

	@Bean
	public MetarManagementOutputPort metarManagementOutputPort(
		MetarManagementRepository repo,
		EntityManager em) {
		return new MetarManagementMySQLAdapter(repo, em);
	}


}
