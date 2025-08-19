package com.ilway.skystat.application.dto.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.WeatherCondition;

public record WeatherStatisticQuery(
	String                    icao,
	RetrievalPeriod period,
	WeatherCondition          condition
) {}
