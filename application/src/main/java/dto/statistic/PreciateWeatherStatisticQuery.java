package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.PredicateWeatherCondition;

public record PreciateWeatherStatisticQuery(
	String                  icao,
	MetarRetrievalPeriod period,
	PredicateWeatherCondition condition
) {}
