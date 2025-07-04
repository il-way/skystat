package dto.taf;

import model.weather.CloudCondition;

import java.time.ZonedDateTime;

public record CloudConditionQuery(
	String                  icao,
	ZonedDateTime           targetTime,
	CloudCondition          condition
) { }
