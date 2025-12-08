package com.ilway.skystat.application.service.metar.fallback;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.port.input.BasicStatisticUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarBasicStatisticFallbackService implements BasicStatisticUseCase {

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
	public List<MonthlyAverageDto> averageMonthly(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		try {
			return dbUseCase.averageMonthly(icao, period, field, unit);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.averageMonthly(icao, period, field, unit);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing statistics", e);
		}
	}
}
