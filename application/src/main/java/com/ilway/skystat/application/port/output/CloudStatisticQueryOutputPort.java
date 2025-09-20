package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.model.weather.WeatherCondition;

import java.time.YearMonth;
import java.util.Map;

public interface CloudStatisticQueryOutputPort {

	Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, CloudCondition condition);

	Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, CloudCondition condition);

}
