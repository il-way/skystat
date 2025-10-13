package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

import java.time.ZonedDateTime;

public record CoverageQueryDto(ZonedDateTime firstTime, ZonedDateTime lastTime, long count) {
}
