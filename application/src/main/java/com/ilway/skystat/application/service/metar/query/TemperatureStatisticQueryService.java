package com.ilway.skystat.application.service.metar.query;

import com.ilway.skystat.application.dto.statistic.temperature.*;
import com.ilway.skystat.application.service.metar.internal.TemperatureStatisticAggregator;
import com.ilway.skystat.application.port.output.TemperatureStatisticQueryOutputPort;
import com.ilway.skystat.application.port.input.TemperatureStatisticUseCase;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.math.RoundingMode.HALF_UP;

@RequiredArgsConstructor
public class TemperatureStatisticQueryService implements TemperatureStatisticUseCase {

	private final TemperatureStatisticQueryOutputPort port;

	@Override
	public TemperatureStatisticResult execute(TemperatureStatisticQuery query) {
		List<DailyTemperatureStatDto> daily = port.findDailyTemperatureStatistic(query.icao(), query.period());
		List<HourlyTemperatureStatDto> hourly = port.findHourlyTemperatureStatistic(query.icao(), query.period());

		return TemperatureStatisticAggregator.aggregate(hourly, daily, RoundingPolicy.of(2, HALF_UP));

	}
}
