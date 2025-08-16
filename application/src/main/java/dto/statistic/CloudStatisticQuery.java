package dto.statistic;

import dto.RetrievalPeriod;
import model.weather.CloudCondition;

public record CloudStatisticQuery(
	String icao,
	RetrievalPeriod period,
	CloudCondition condition
) {}
