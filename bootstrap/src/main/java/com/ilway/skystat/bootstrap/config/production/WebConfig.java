package com.ilway.skystat.bootstrap.config.production;

import com.ilway.skystat.bootstrap.profile.Production;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Production
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("https://www.sky-stat.com", "https://sky-stat.com")
			.allowedMethods("*");
	}

}
