package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;

import java.util.List;

public interface MetarStatisticSummaryQueryOutputPort {

	AverageSummary averageSummary(String icao, RetrievalPeriod period);

	List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target);

}
