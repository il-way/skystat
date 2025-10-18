package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;

import java.time.ZonedDateTime;
import java.util.List;

public record ObservationStatisticResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	List<MonthlyCountDto> monthlyData,
	List<HourlyCountDto> hourlyData
) {

	public static ObservationStatisticResponse from(PeriodInventory inventory, ObservationStatisticResult result) {
		return new ObservationStatisticResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			inventory.totalCount(),
			result.monthly(),
			result.hourly()
		);
	}

}
