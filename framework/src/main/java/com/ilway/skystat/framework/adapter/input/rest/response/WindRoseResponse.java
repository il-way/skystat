package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.windrose.WindRose;

import java.time.Month;
import java.util.Map;

public record WindRoseResponse(
	long metarSize,
	Map<Month, WindRose> data

) {
}
