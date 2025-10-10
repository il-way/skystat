package com.ilway.skystat.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ilway.skystat")
@EntityScan(basePackages = "com.ilway.skystat.framework")
@EnableJpaRepositories(basePackages = "com.ilway.skystat.framework")
@ConfigurationPropertiesScan("com.ilway.skystat")
public class AppConfig {

}
