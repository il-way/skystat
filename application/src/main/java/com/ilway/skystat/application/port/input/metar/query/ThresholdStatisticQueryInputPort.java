package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

@RequiredArgsConstructor
public class ThresholdStatisticQueryInputPort implements StatisticUseCase<ThresholdStatisticQuery> {

	private final ThresholdStatisticQueryOutputPort port;

	@Override
	public ObservationStatisticResult execute(ThresholdStatisticQuery query) {
		Map<YearMonth, Long> countMonthly = port.countDistinctDaysByMonth(query.icao(), query.period(), query.condition());
		Map<YearMonth, Map<Integer, Long>> countHourly = port.countDistinctHoursByMonth(query.icao(), query.period(), query.condition());

		return ObservationStatisticAggregator.aggregate(countMonthly, countHourly, query.period());
	}
}
