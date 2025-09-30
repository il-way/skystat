package com.ilway.skystat.application.port.input.metar.delegator;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
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
		} catch (Exception e) {
			return scanUseCase.execute(query);
		}
	}

}
