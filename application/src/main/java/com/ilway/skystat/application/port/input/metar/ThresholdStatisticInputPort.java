package com.ilway.skystat.application.port.input.metar;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.ThresholdCondition;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ThresholdStatisticInputPort implements StatisticUseCase<ThresholdStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResult execute(ThresholdStatisticQuery query) {
		List<Metar> metars = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		ThresholdCondition condition = query.condition();
		Predicate<Metar> predicate = m -> {
			double value = condition.field().extract(m, condition.unit());
			return condition.comparison().test(value, condition.threshold());
		};

		return ObservationStatisticAggregator.aggregate(metars, predicate, query.period());
	}

}