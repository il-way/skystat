package com.ilway.skystat.application.dto.statistic.temperature;

import com.ilway.skystat.application.dto.RetrievalPeriod;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;

public record TemperatureStatisticQuery(
	String icao,
	RetrievalPeriod period
) {

	public static  TemperatureStatisticQuery of(String icao, int yearInclusive, int yearExclusive) {
		return new TemperatureStatisticQuery(icao, new RetrievalPeriod(
			ofLenientUtc(yearInclusive, 1, 1, 0, 0),
			ofLenientUtc(yearExclusive, 1, 1, 0, 0)
		));
	}

}

