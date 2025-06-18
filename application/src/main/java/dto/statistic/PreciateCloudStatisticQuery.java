package dto.statistic;

import dto.MetarRetrievalPeriod;
import model.PredicateCloudCondition;

public record PreciateCloudStatisticQuery(
	String                  icao,
	MetarRetrievalPeriod    period,
	PredicateCloudCondition condition
) {}
