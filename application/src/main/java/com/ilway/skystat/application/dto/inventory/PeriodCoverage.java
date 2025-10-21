package com.ilway.skystat.application.dto.inventory;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import lombok.Builder;

import java.time.ZonedDateTime;

public record PeriodCoverage(
	ZonedDateTime firstInPeriod,
	ZonedDateTime lastInPeriod,
	long totalCount
) {

	public boolean hasData() {
		return totalCount > 0;
	}

	public static PeriodCoverage empty(String icao, RetrievalPeriod period) {
		return new PeriodCoverage(null, null, 0);
	}
}
