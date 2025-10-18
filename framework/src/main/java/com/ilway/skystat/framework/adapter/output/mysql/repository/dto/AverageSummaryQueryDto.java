package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record AverageSummaryQueryDto(
	double avgVisibilityM,
	double avgWindSpeedKt,
	double avgWindPeakKt,
	double avgAltimeterHpa,
	double avgCeilingFt
	) {
}
