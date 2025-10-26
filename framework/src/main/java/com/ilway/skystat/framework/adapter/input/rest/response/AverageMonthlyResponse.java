package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;

import java.time.ZonedDateTime;
import java.util.List;

public record AverageMonthlyResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	List<MonthlyAverageDto> monthly
) {

	public static AverageMonthlyResponse from(PeriodInventory inventory, List<MonthlyAverageDto> monthly) {
		if (!inventory.hasData()) return empty();
		return new AverageMonthlyResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			inventory.totalCount(),
			monthly
		);
	}

	public static AverageMonthlyResponse empty() {
		return new AverageMonthlyResponse(
			null,
			null,
			0,
			List.of()
		);
	}

}
