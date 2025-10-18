package com.ilway.skystat.application.dto.statistic;

public record AverageSummary(
	double avgVisibilityM,
	double avgWindSpeedKt,
	double avgWindPeakKt,
	double avgAltimeterHpa,
	double avgCeilingFt
) {
}
