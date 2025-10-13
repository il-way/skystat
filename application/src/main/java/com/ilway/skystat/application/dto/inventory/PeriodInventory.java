package com.ilway.skystat.application.dto.inventory;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record PeriodInventory(
	String icao,
	RetrievalPeriod requestPeriod,
	ZonedDateTime firstReportTime,
	ZonedDateTime lastReportTime,
	ZonedDateTime firstAvailableTime,
	ZonedDateTime lastAvailableTime,
	long totalCount
) {

	public boolean hasData() {
		return totalCount > 0;
	}

	public static PeriodInventory empty(String icao, RetrievalPeriod period, ZonedDateTime firstReportTime, ZonedDateTime lastReportTime) {
		return PeriodInventory.builder()
			       .icao(icao)
			       .firstReportTime(firstReportTime)
			       .lastReportTime(lastReportTime)
			       .firstAvailableTime(null)
			       .lastAvailableTime(null)
			       .requestPeriod(period)
			       .totalCount(0)
			       .build();
	}

}
