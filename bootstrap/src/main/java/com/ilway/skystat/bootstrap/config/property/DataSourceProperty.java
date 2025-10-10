package com.ilway.skystat.bootstrap.config.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("spring.datasource")
@Validated
public class DataSourceProperty {

	@NotBlank
	private final String url;

}
