package com.ilway.skystat.framework.adapter.output.mysql.data.converter;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Converter
public class WeatherDescriptorConverter extends AbstractWeatherConverter<WeatherDescriptor> {

	@Override
	public String convertToDatabaseColumn(List<WeatherDescriptor> attribute) {
		return attribute.stream()
			       .map(String::valueOf)
			       .collect(Collectors.joining(""));
	}

	@Override
	public List<WeatherDescriptor> convertToEntityAttribute(String dbData) {
		return mapPairs(dbData, WeatherDescriptor::valueOf);
	}
}
