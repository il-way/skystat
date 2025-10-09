package com.ilway.skystat.application.dto.statistic;

public record HourlyCountDto(
	int year,
	int month,
	int hour,
	long count
) {
}
