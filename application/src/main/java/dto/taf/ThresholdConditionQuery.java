package dto.taf;

import model.weather.ThresholdCondition;

import java.time.ZonedDateTime;

public record ThresholdConditionQuery(
	String              icao,
	ZonedDateTime       targetTime,
	ThresholdCondition  condition
) { }
