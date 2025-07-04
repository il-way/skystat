package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.weather.CloudCondition;

public record CloudStatisticQuery(
	String                  icao,
	MetarRetrievalPeriod    period,
	CloudCondition condition
) {}
