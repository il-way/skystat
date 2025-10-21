package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record AverageSummaryQueryDto(
	Double avgVisibilityM,
	Double avgWindSpeedKt,
	Double avgWindPeakKt,
	Double avgAltimeterHpa,
	Double avgCeilingFt
	) {
}
