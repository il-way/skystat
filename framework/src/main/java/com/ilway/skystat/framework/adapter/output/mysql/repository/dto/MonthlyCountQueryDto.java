package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record MonthlyCountQueryDto(Integer year, Integer month, Long count) {
}
