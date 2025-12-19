package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.DatasetCoverage;

import java.time.ZonedDateTime;

public record DatasetCoverageResponse(
	String icao,
	ZonedDateTime firstReportTime,
	ZonedDateTime lastReportTime,
	long totalCount
) {

	public static DatasetCoverageResponse from(String icao, DatasetCoverage datasetCoverage) {
		if (!datasetCoverage.hasData()) return empty(icao);
		return new DatasetCoverageResponse(
			icao,
			datasetCoverage.firstReportTime(),
			datasetCoverage.lastReportTime(),
			datasetCoverage.totalCount()
		);
	}

	public static DatasetCoverageResponse empty(String icao) {
		return new DatasetCoverageResponse(
			icao,
			null,
			null,
			0
		);
	}

}
