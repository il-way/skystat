package com.ilway.skystat.application.dto.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.model.weather.ThresholdCondition;

public record ThresholdStatisticQuery(
	String                icao,
	RetrievalPeriod       period,
	ThresholdCondition    condition
) {}
