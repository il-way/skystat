package com.ilway.skystat.application.dto.windrose;

import java.time.Month;
import java.util.Map;

public record WindRoseResult(
	int totalCount,
	int sampleSize,
	int missingCount,
	Map<Month, WindRose> windRoseMap
) {
}
