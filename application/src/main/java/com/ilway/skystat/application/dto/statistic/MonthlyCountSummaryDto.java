package com.ilway.skystat.application.dto.statistic;

public record MonthlyCountSummaryDto(
	int year,
	int month,
	long windPeakCount,
	long visibilityCount,
	long ceilingCount,
	long phenomenonCount,
	long descriptorCount) {
}
