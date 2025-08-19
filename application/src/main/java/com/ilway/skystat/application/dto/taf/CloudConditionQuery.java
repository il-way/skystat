package com.ilway.skystat.application.dto.taf;

import com.ilway.skystat.application.model.weather.CloudCondition;

import java.time.ZonedDateTime;

public record CloudConditionQuery(
	String                  icao,
	ZonedDateTime           targetTime,
	CloudCondition          condition
) { }
