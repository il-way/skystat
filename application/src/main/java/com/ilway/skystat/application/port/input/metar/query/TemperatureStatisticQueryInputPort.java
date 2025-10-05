package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.statistic.temperature.*;
import com.ilway.skystat.application.port.input.internal.TemperatureStatisticAggregator;
import com.ilway.skystat.application.port.output.TemperatureStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TemperatureStatisticQueryInputPort implements TemperatureStatisticUseCase {

	private final TemperatureStatisticQueryOutputPort port;

	@Override
	public TemperatureStatisticResult execute(TemperatureStatisticQuery query) {
		List<DailyTemperatureStatDto> daily = port.findDailyTemperatureStatistic(query.icao(), query.period());
		List<HourlyTemperatureStatDto> hourly = port.findHourlyTemperatureStatistic(query.icao(), query.period());
		List<MonthlyTemperatureStatDto> monthly = TemperatureStatisticAggregator.monthly(daily);
		List<YearlyTemperatureStatDto> yearly = TemperatureStatisticAggregator.yearly(daily);

		return new TemperatureStatisticResult(monthly, hourly, yearly);
	}
}
