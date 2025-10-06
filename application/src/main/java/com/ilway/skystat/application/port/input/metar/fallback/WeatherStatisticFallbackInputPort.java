package com.ilway.skystat.application.port.input.metar.fallback;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
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
		} catch (AggregationUnavailableException e) {
			return scanUseCase.execute(query);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing Weather statistics", e);
		}
	}
}
