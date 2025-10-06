package com.ilway.skystat.application.port.input.metar.fallback;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThresholdStatisticFallbackInputPort implements StatisticUseCase<ThresholdStatisticQuery>  {

	private final StatisticUseCase<ThresholdStatisticQuery> dbUseCase;
	private final StatisticUseCase<ThresholdStatisticQuery> scanUseCase;

	@Override
	public ObservationStatisticResult execute(ThresholdStatisticQuery query) {
		try {
			return dbUseCase.execute(query);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.execute(query);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing Threshold statistics", e);
		}
	}

}
