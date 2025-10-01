package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record MonthlyCountQueryDto(int year, int month, long count) {
}
