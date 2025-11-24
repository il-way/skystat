package com.ilway.skystat.application.port.input.metar.fallback;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.application.usecase.StatisticSummaryUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarStatisticSummaryFallbackInputPort implements StatisticSummaryUseCase {

	private final StatisticSummaryUseCase dbUseCase;
	private final StatisticSummaryUseCase scanUseCase;

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

	@Override
	public List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target) {
		try {
			return dbUseCase.countSummaryByMonth(icao, period, target);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.countSummaryByMonth(icao, period, target);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while processing statistics", e);
		}
	}

}
