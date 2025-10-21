package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.MonthlyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;
import com.ilway.skystat.application.dto.statistic.temperature.YearlyTemperatureStatDto;

import java.time.ZonedDateTime;
import java.util.List;

public record TemperatureStatisticResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	List<MonthlyTemperatureStatDto> monthly,
	List<HourlyTemperatureStatDto> hourly,
	List<YearlyTemperatureStatDto> yearly
) {

	public static TemperatureStatisticResponse from(PeriodInventory inventory, TemperatureStatisticResult result) {
		if (!inventory.hasData()) return new TemperatureStatisticResponse(
			null, null, 0, List.of(), List.of(), List.of()
		);

		return new TemperatureStatisticResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			inventory.totalCount(),
			result.monthly(),
			result.hourly(),
			result.yearly()
		);
	}
}
