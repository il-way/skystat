package dto.statistic;

import dto.RetrievalPeriod;
import model.weather.WeatherCondition;

public record WeatherStatisticQuery(
	String                    icao,
	RetrievalPeriod period,
	WeatherCondition          condition
) {}
