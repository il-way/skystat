package com.ilway.skystat.framework.config;

import com.ilway.skystat.framework.web.filter.CorrelationIdFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

	@Bean
	public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
		FilterRegistrationBean<CorrelationIdFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorrelationIdFilter());
		bean.setOrder(0);
		return bean;
	}

}
