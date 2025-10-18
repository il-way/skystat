package com.ilway.skystat.application.port.input.metar.fallback;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MetarBasicStatisticFallbackInputPort implements BasicStatisticUseCase {

	private final BasicStatisticUseCase dbUseCase;
	private final BasicStatisticUseCase scanUseCase;

	@Override
	public double average(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		try {
			return dbUseCase.average(icao, period, field, unit);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.average(icao, period, field, unit);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing statistics", e);
		}
	}

	@Override
	public AverageSummary averageSummary(String icao, RetrievalPeriod period) {
		try {
			return dbUseCase.averageSummary(icao, period);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.averageSummary(icao, period);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing statistics", e);
		}
	}
}
