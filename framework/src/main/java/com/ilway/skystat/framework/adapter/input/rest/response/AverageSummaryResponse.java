package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;

import java.time.ZonedDateTime;

public record AverageSummaryResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	double avgVisibilityM,
	double avgWindSpeedKt,
	double avgWindPeakKt,
	double avgAltimeterHpa,
	double avgCeilingFt
) {

	public static AverageSummaryResponse from(PeriodInventory inventory, AverageSummary summary) {
		return new AverageSummaryResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			inventory.totalCount(),
			summary.avgVisibilityM(),
			summary.avgWindSpeedKt(),
			summary.avgWindPeakKt(),
			summary.avgAltimeterHpa(),
			summary.avgCeilingFt()
		);
	}

}
