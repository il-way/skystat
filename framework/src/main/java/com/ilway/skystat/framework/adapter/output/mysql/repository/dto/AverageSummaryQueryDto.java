package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

import com.ilway.skystat.application.dto.statistic.AverageSummary;

public record AverageSummaryQueryDto(
	Double avgVisibilityM,
	Double avgWindSpeedKt,
	Double avgWindPeakKt,
	Double avgAltimeterHpa,
	Double avgCeilingFt
) {

	public static AverageSummary map(AverageSummaryQueryDto dto) {
		if (dto == null) {
			return new AverageSummary(0, 0, 0, 0, 0);
		}
		return new AverageSummary(
			nvl(dto.avgVisibilityM()),
			nvl(dto.avgWindSpeedKt()),
			nvl(dto.avgWindPeakKt()),
			nvl(dto.avgAltimeterHpa()),
			nvl(dto.avgCeilingFt())
		);
	}

	private static double nvl(Double v) {
		return v != null ? v : 0d;
	}

}
