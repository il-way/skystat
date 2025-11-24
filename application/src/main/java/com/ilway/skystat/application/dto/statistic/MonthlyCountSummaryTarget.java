package com.ilway.skystat.application.dto.statistic;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

public record MonthlyCountSummaryTarget(
	double windPeakThreshold,
	double visibilityThreshold,
	double ceilingThreshold,
	WeatherPhenomenon phenomenon,
	WeatherDescriptor descriptor) {
}
