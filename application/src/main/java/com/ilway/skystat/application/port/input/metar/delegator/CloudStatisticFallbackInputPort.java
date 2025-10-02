package com.ilway.skystat.application.port.input.metar.delegator;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloudStatisticFallbackInputPort implements StatisticUseCase<CloudStatisticQuery> {

	private final StatisticUseCase<CloudStatisticQuery> dbUseCase;
	private final StatisticUseCase<CloudStatisticQuery> scanUseCase;

	@Override
	public ObservationStatisticResult execute(CloudStatisticQuery query) {
		try {
			return dbUseCase.execute(query);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.execute(query);
		}
	}
}
