package com.ilway.skystat.application.dto.statistic.temperature;

public record TemperatureStatisticResult(
	MonthlyTemperatureStatDto monthly,
	HourlyTemperatureStatDto hourly,
	YearlyTemperatureStatDto yearly) {
}
