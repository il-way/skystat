package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;

import java.time.ZonedDateTime;
import java.util.List;

public record MonthlyCountSummaryResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	List<MonthlyCountSummaryDto> monthly
) {

	public static MonthlyCountSummaryResponse from(PeriodInventory inventory, List<MonthlyCountSummaryDto> monthly) {
		if (!inventory.hasData()) return empty();
		return new MonthlyCountSummaryResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			inventory.totalCount(),
			monthly
		);
	}

	public static MonthlyCountSummaryResponse empty() {
		return new MonthlyCountSummaryResponse(
			null,
			null,
			0,
			List.of()
		);
	}

}
