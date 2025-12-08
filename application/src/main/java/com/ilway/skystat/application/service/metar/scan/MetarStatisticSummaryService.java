package com.ilway.skystat.application.service.metar.scan;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.input.StatisticSummaryUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarStatisticSummaryService implements StatisticSummaryUseCase {

	private final MetarManagementOutputPort outputPort;

	@Override
	public AverageSummary averageSummary(String icao, RetrievalPeriod period) {
		return null;
	}

	@Override
	public List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target) {
		return List.of();
	}

}
