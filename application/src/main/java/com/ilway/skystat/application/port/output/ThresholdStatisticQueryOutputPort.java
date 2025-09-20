package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.ThresholdCondition;

import java.time.YearMonth;
import java.util.Map;

public interface ThresholdStatisticQueryOutputPort {

	Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition);

	Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition);

}
