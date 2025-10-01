package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record HourlyCountQueryDto(int year, int month, int hour, long count) {
}
