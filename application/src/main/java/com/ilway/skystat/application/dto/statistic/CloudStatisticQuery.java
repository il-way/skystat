package com.ilway.skystat.application.dto.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.CloudCondition;

public record CloudStatisticQuery(
	String icao,
	RetrievalPeriod period,
	CloudCondition condition
) {}
