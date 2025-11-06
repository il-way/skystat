package com.ilway.skystat.application.dto.management;

import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.Instant;

public record UniqueKey(String icao, Instant obsTime, String rawText) {
	public static UniqueKey from(Metar m) {
		return new UniqueKey(m.getStationIcao(), m.getObservationTime().toInstant(), m.getRawText());
	}
}
