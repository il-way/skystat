package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.model.weather.ThresholdCondition;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.port.output.CloudStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;

import java.time.YearMonth;
import java.util.Map;

public class MetarStatisticQueryMySqlAdapter implements WeatherStatisticQueryOutputPort, CloudStatisticQueryOutputPort, ThresholdStatisticQueryOutputPort {

	@Override
	public Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, CloudCondition condition) {
		return Map.of();
	}

	@Override
	public Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, CloudCondition condition) {
		return Map.of();
	}

	@Override
	public Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition) {
		return Map.of();
	}

	@Override
	public Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition) {
		return Map.of();
	}

	@Override
	public Map<YearMonth, Long> countDistinctDaysByMonth(String icao, RetrievalPeriod period, WeatherCondition condition) {
		return Map.of();
	}

	@Override
	public Map<YearMonth, Map<Integer, Long>> countDistinctHoursByMonth(String icao, RetrievalPeriod period, WeatherCondition condition) {
		return Map.of();
	}
}
