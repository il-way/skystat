package com.ilway.skystat.application.dto.inventory;

import java.time.ZonedDateTime;

public record DatasetCoverage(
	ZonedDateTime firstReportTime,
	ZonedDateTime lastReportTime,
	long totalCount
) {
	public static DatasetCoverage empty(String icao) {
		return new DatasetCoverage(null, null, 0);
	}
}
