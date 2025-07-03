package dto.taf;

import model.weather.WeatherConditionPredicate;

import java.time.ZonedDateTime;

public record WeatherConditionQuery(
	String                    icao,
	ZonedDateTime             targetTime,
	WeatherConditionPredicate condition
) { }
