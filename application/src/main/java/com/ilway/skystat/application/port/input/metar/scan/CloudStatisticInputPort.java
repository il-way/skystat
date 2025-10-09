package com.ilway.skystat.application.port.input.metar.scan;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CloudStatisticInputPort implements StatisticUseCase<CloudStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResult execute(CloudStatisticQuery query) {
		List<Metar> metars = metarManagementOutputPort.findByIcaoAndReportTimePeriod(query.icao(), query.period());

		CloudCondition condition = query.condition();
		Predicate<Metar> predicate = m -> condition.predicate().test(m.getClouds(), condition.target());

		return ObservationStatisticAggregator.aggregate(metars, predicate, query.period());
	}
}
