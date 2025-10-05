package com.ilway.skystat.application.dto.statistic.temperature;

public record DailyTemperatureStatDto(
	int year, int month, int day,
	Double dailyMean,
	Double dailyMax,
	Double dailyMin
) {
}
