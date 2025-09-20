package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.WeatherCondition;

import java.time.YearMonth;
import java.util.Map;

public interface WeatherStatisticQueryOutputPort {

	Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, WeatherCondition condition);

	Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, WeatherCondition condition);

}
