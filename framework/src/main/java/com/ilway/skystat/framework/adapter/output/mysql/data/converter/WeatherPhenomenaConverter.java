package com.ilway.skystat.framework.adapter.output.mysql.data.converter;

import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.stream.Collectors;

@Converter
public class WeatherPhenomenaConverter extends AbstractWeatherConverter<WeatherPhenomenon> {

	@Override
	public String convertToDatabaseColumn(List<WeatherPhenomenon> attribute) {
		return attribute.stream()
			       .map(String::valueOf)
			       .collect(Collectors.joining(""));
	}

	@Override
	public List<WeatherPhenomenon> convertToEntityAttribute(String dbData) {
		return mapPairs(dbData, WeatherPhenomenon::valueOf);
	}
}
