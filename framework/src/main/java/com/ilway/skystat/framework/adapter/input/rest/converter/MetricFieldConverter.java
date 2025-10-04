package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.application.model.weather.MetricField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricFieldConverter implements Converter<String, MetricField> {
	@Override
	public MetricField convert(String source) {
		String s = source.trim().toLowerCase();
		return switch (s) {
			case "ceiling" -> MetricField.LOWEST_CEILING;
			case "visibility" -> MetricField.VISIBILITY;
			case "windpeak" -> MetricField.WIND_PEAK;
			case "windspeed" -> MetricField.WIND_SPEED;
			case "altimeter" -> MetricField.ALTIMETER;
			default -> throw new IllegalArgumentException("Unsupported field: " + s);
		};
	}
}
