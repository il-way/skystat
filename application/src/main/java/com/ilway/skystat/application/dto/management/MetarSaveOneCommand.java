package com.ilway.skystat.application.dto.management;

import java.time.ZonedDateTime;

public record MetarSaveOneCommand(
	String icao,
	String rawText,
	ZonedDateTime observationTime
) {
}
