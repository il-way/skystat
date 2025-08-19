package com.ilway.skystat.application.dto.taf;

import com.ilway.skystat.application.model.weather.ThresholdCondition;

import java.time.ZonedDateTime;

public record ThresholdConditionQuery(
	String              icao,
	ZonedDateTime       targetTime,
	ThresholdCondition  condition
) { }
