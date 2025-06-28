package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.weather.WeatherConditionPredicate;

public record WeatherStatisticQuery(
	String                    icao,
	MetarRetrievalPeriod      period,
	WeatherConditionPredicate condition
) {}
