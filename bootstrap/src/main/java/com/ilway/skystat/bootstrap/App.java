package com.ilway.skystat.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ilway.skystat")
@EntityScan(basePackages = "com.ilway.skystat.framework")
@EnableJpaRepositories(basePackages = "com.ilway.skystat.framework")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
