package com.ilway.skystat.framework.adapter.output.mysql.data.converter;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public abstract class AbstractWeatherConverter<T> implements AttributeConverter<List<T>, String> {

	protected List<T> mapPairs(String s, Function<String, T> mapper) {
		if (s==null || s.isBlank()) return List.of();
		if ((s.length() & 1) == 1) {
			throw new IllegalArgumentException("Expected even length, but was " + s.length());
		}

		return IntStream.range(0, s.length()/2)
			       .mapToObj(i -> mapper.apply(s.substring(2*i, 2*(i+1))))
			       .toList();
	}

}
