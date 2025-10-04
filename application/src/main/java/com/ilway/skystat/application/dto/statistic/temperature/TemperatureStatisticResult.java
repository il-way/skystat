package com.ilway.skystat.application.dto.statistic.temperature;

import java.util.List;

public record TemperatureStatisticResult(
	List<MonthlyTemperatureStatDto> monthly,
	List<HourlyTemperatureStatDto> hourly,
	List<YearlyTemperatureStatDto> yearly) {
}
