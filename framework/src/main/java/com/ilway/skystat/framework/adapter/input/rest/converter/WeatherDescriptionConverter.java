package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.domain.vo.weather.type.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherDescriptionConverter implements Converter<String, WeatherDescription> {

	@Override
	public WeatherDescription convert(String source) {
		if (source == null) return null;
		String s = source.trim().toUpperCase();

		for (WeatherDescriptor wd : WeatherDescriptor.values()) {
			if (wd.name().equals(s)) return wd;
		}

		for (WeatherPhenomenon wp : WeatherPhenomenon.values()) {
			if (wp.name().equals(s)) return wp;
		}

		for (CloudType wd : CloudType.values()) {
			if (wd.name().equals(s)) return wd;
		}

		for (CloudCoverage wd : CloudCoverage.values()) {
			if (wd.name().equals(s)) return wd;
		}

		throw new IllegalArgumentException("Unsupported weather description: " + source);
	}
}
