package dto.taf;

import model.weather.CloudConditionPredicate;

import java.time.ZonedDateTime;

public record CloudConditionQuery(
	String                  icao,
	ZonedDateTime           targetTime,
	CloudConditionPredicate condition
) { }
