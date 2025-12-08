package com.ilway.skystat.application.service.metar.fallback;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.input.StatisticUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloudStatisticFallbackService implements StatisticUseCase<CloudStatisticQuery> {

	private final StatisticUseCase<CloudStatisticQuery> dbUseCase;
	private final StatisticUseCase<CloudStatisticQuery> scanUseCase;

	@Override
	public ObservationStatisticResult execute(CloudStatisticQuery query) {
		try {
			return dbUseCase.execute(query);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.execute(query);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing Cloud statistics", e);
		}
	}
}
