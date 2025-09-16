package com.ilway.skystat.it;

import com.ilway.skystat.it.config.UseCaseConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({UseCaseConfig.class})
@SpringBootApplication(scanBasePackages = "com.ilway.skystat")
@EnableJpaRepositories(basePackages = "com.ilway.skystat")
@EntityScan(basePackages = "com.ilway.skystat")
public class ItTestApp {
}
