package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.weather.ThresholdCondition;

public record ThresholdStatisticQuery(
	String                icao,
	MetarRetrievalPeriod  period,
	ThresholdCondition    condition
) {}
