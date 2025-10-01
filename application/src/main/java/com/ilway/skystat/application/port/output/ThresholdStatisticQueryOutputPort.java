package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.model.weather.ThresholdCondition;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface ThresholdStatisticQueryOutputPort {

	List<MonthlyCountDto> countDistinctDaysByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition);

	List<HourlyCountDto> countDistinctHoursByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition);

}
