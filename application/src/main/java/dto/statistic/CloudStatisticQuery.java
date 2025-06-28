package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.weather.CloudConditionPredicate;

public record CloudStatisticQuery(
	String                  icao,
	MetarRetrievalPeriod    period,
	CloudConditionPredicate condition
) {}
