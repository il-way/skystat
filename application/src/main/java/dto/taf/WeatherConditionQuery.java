package dto.taf;

import model.weather.WeatherCondition;

import java.time.ZonedDateTime;

public record WeatherConditionQuery(
	String                    icao,
	ZonedDateTime             targetTime,
	WeatherCondition condition
) { }
