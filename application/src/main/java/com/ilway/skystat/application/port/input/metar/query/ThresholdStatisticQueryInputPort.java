package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ThresholdStatisticQueryInputPort implements StatisticUseCase<ThresholdStatisticQuery> {

	private final ThresholdStatisticQueryOutputPort port;

	@Override
	public ObservationStatisticResult execute(ThresholdStatisticQuery query) {
		List<MonthlyCountDto> monthly = port.countDistinctDaysByMonth(query.icao(), query.period(), query.condition());
		List<HourlyCountDto> hourly = port.countDistinctHoursByMonth(query.icao(), query.period(), query.condition());

		return ObservationStatisticAggregator.aggregate(monthly, hourly, query.period());
	}
}
