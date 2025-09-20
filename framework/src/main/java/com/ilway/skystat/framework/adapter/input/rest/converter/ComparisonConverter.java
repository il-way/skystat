package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.application.model.generic.Comparison;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ComparisonConverter implements Converter<String, Comparison> {
	@Override
	public Comparison convert(String source) {
		if (source == null) return null;
		try {
			String s = source.trim().toUpperCase();
			return Comparison.valueOf(s);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unsupported Comparison: " + source);
		}
	}
}
