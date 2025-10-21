package com.ilway.skystat.application.dto.inventory;

import java.time.ZonedDateTime;

public record DatasetCoverage(
	ZonedDateTime firstReportTime,
	ZonedDateTime lastReportTime,
	long totalCount
) {

	public boolean hasData() {
		return totalCount > 0;
	}

	public static DatasetCoverage empty(String icao) {
		return new DatasetCoverage(null, null, 0);
	}
}
