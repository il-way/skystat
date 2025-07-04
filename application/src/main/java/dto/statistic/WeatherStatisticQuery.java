package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.weather.WeatherCondition;

public record WeatherStatisticQuery(
	String                    icao,
	MetarRetrievalPeriod      period,
	WeatherCondition          condition
) {}
