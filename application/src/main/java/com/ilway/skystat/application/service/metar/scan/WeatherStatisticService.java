package com.ilway.skystat.application.service.metar.scan;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.service.metar.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.input.StatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class WeatherStatisticService implements StatisticUseCase<WeatherStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResult execute(WeatherStatisticQuery query) {
		List<Metar> metars = metarManagementOutputPort.findByIcaoAndReportTimePeriod(query.icao(), query.period());

		WeatherCondition condition = query.condition();
		Predicate<Metar> predicate = m -> condition.predicate().test(m.getWeathers(), condition.target());

		return ObservationStatisticAggregator.aggregate(metars, predicate, query.period());
	}
}
