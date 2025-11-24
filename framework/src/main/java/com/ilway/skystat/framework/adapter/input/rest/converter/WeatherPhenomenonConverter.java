package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherPhenomenonConverter implements Converter<String, WeatherPhenomenon> {

	@Override
	public WeatherPhenomenon convert(String source) {
		if (source == null) return null;
		String s = source.trim().toUpperCase();
		for (WeatherPhenomenon wp : WeatherPhenomenon.values()) {
			if (wp.name().equals(s)) return wp;
		}
		throw new IllegalArgumentException("Unsupported weather phenomenon: " + source);
	}
}
