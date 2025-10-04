package com.ilway.skystat.application.dto.statistic.temperature;

public record HourlyTemperatureStatDto(
	int year,
	int month,
	int hour,
	Double meanTempAtHour,
	Double maxTempAtHour,
	Double minTempAtHour
) {
}
