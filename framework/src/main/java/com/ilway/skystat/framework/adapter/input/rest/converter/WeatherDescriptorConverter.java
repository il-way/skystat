package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherDescriptorConverter implements Converter<String, WeatherDescriptor> {

	@Override
	public WeatherDescriptor convert(String source) {
		if (source == null) return null;
		String s = source.trim().toUpperCase();
		for (WeatherDescriptor wd : WeatherDescriptor.values()) {
			if (wd.name().equals(s)) return wd;
		}
		throw new IllegalArgumentException("Unsupported weather descriptor: " + source);
	}
}
