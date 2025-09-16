package com.ilway.skystat.framework;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ilway.skystat.framework")
@EnableJpaRepositories(basePackages = "com.ilway.skystat.framework")
public class FrameworkTestApp {
}
