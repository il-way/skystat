package com.ilway.skystat.application.dto.statistic;

public record HourlyCountDto(
	int year,
	int month,
	int hourUtc,
	long count
) {
}
