package com.ilway.skystat.application.port.input.metar.scan;

import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticQuery;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;
import com.ilway.skystat.application.port.input.internal.TemperatureStatisticAggregator;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.domain.vo.metar.Metar;
import lombok.RequiredArgsConstructor;

import java.math.RoundingMode;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

@RequiredArgsConstructor
public class TemperatureStatisticInputPort implements TemperatureStatisticUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public TemperatureStatisticResult execute(TemperatureStatisticQuery query) {
		List<Metar> metars = metarManagementOutputPort.findByIcaoAndReportTimePeriod(query.icao(), query.period());
		return TemperatureStatisticAggregator.aggregate(metars, query.period(), RoundingPolicy.of(2, HALF_UP));
	}
}
