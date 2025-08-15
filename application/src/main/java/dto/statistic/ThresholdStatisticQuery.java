package dto.statistic;

import dto.RetrievalPeriod;
import model.weather.ThresholdCondition;

public record ThresholdStatisticQuery(
	String                icao,
	RetrievalPeriod period,
	ThresholdCondition    condition
) {}
