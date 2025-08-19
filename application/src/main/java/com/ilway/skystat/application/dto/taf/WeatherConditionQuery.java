package com.ilway.skystat.application.dto.taf;

import com.ilway.skystat.application.model.weather.WeatherCondition;

import java.time.ZonedDateTime;

public record WeatherConditionQuery(
	String                    icao,
	ZonedDateTime             targetTime,
	WeatherCondition condition
) { }
