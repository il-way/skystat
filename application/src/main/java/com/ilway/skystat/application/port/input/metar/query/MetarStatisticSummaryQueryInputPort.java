package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.port.output.MetarStatisticSummaryQueryOutputPort;
import com.ilway.skystat.application.usecase.StatisticSummaryUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarStatisticSummaryQueryInputPort implements StatisticSummaryUseCase {

	private final MetarStatisticSummaryQueryOutputPort outputPort;

	@Override
	public AverageSummary averageSummary(String icao, RetrievalPeriod period) {
		return outputPort.averageSummary(icao, period);
	}

	@Override
	public List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target) {
		return outputPort.countSummaryByMonth(icao, period, target);
	}

}
