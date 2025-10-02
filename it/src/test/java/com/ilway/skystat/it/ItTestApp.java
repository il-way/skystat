package com.ilway.skystat.it;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ilway.skystat")
@EnableJpaRepositories(basePackages = "com.ilway.skystat")
@EntityScan(basePackages = "com.ilway.skystat")
public class ItTestApp {
}
