package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

@RequiredArgsConstructor
public class WeatherStatisticQueryInputPort implements StatisticUseCase<WeatherStatisticQuery> {

	private final WeatherStatisticQueryOutputPort port;

	@Override
	public ObservationStatisticResponse execute(WeatherStatisticQuery query) {
		Map<YearMonth, Long> countMonthly = port.countDistinctDaysByMonth(query.icao(), query.period(), query.condition());
		Map<YearMonth, Map<Integer, Long>> countHourly = port.countDistinctHoursByMonth(query.icao(), query.period(), query.condition());

		return ObservationStatisticAggregator.aggregate(countMonthly, countHourly, query.period());
	}
}
