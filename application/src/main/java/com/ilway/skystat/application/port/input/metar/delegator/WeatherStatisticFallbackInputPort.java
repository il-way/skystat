package com.ilway.skystat.application.port.input.metar.delegator;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherStatisticFallbackInputPort implements StatisticUseCase<WeatherStatisticQuery> {

	private final StatisticUseCase<WeatherStatisticQuery> dbUseCase;
	private final StatisticUseCase<WeatherStatisticQuery> scanUseCase;

	@Override
	public ObservationStatisticResult execute(WeatherStatisticQuery query) {
		try {
			return dbUseCase.execute(query);
		} catch (Exception e) {
			return scanUseCase.execute(query);
		}
	}
}
