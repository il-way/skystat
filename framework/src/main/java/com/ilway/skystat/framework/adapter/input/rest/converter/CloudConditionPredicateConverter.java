package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.application.model.weather.CloudConditionPredicate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CloudConditionPredicateConverter implements Converter<String, CloudConditionPredicate> {
	@Override
	public CloudConditionPredicate convert(String source) {
		String s = source.trim().toLowerCase();
		return switch (s) {
			case "type" -> CloudConditionPredicate.HAS_CLOUDTYPE;
			case "coverage" -> CloudConditionPredicate.HAS_COVERAGE;
			default -> throw new IllegalArgumentException("Unsupported field: " + s);
		};
	}
}
