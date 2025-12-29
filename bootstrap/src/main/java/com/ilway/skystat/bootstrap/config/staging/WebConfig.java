package com.ilway.skystat.bootstrap.config.staging;

import com.ilway.skystat.bootstrap.profile.Production;
import com.ilway.skystat.bootstrap.profile.Staging;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Staging
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173")
			.allowedMethods("*");
	}

}
