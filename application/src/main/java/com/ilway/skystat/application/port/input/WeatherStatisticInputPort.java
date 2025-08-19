package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class WeatherStatisticInputPort implements StatisticUseCase<WeatherStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(WeatherStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		WeatherCondition condition = query.condition();
		Predicate<Metar> predicate = m -> condition.predicate().test(m.getWeatherGroup(), condition.target());

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
