package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;

import java.util.List;

public interface StatisticSummaryUseCase {

	AverageSummary averageSummary(String icao, RetrievalPeriod period);

	List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target);

}
