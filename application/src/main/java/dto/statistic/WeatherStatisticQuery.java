package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.WeatherConditionPredicate;

public record WeatherStatisticQuery(
	String                    icao,
	MetarRetrievalPeriod      period,
	WeatherConditionPredicate condition
) {}
