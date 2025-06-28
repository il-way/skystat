package dto.query;

import model.weather.WeatherConditionPredicate;

import java.time.ZonedDateTime;

public record WeatherConditionQuery(
	String                    icao,
	ZonedDateTime             targetTime,
	WeatherConditionPredicate condition
) { }
