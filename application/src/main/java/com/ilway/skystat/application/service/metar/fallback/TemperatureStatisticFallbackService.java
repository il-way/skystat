package com.ilway.skystat.application.service.metar.fallback;

import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticQuery;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.input.TemperatureStatisticUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TemperatureStatisticFallbackService implements TemperatureStatisticUseCase {

	private final TemperatureStatisticUseCase dbUseCase;
	private final TemperatureStatisticUseCase scanUseCase;

	@Override
	public TemperatureStatisticResult execute(TemperatureStatisticQuery query) {
		try {
			return dbUseCase.execute(query);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.execute(query);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing Temperature statistics", e);
		}
	}
}
