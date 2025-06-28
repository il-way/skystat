package dto.query;

import model.weather.CloudConditionPredicate;

import java.time.ZonedDateTime;

public record CloudConditionQuery(
	String                  icao,
	ZonedDateTime           targetTime,
	CloudConditionPredicate condition
) { }
