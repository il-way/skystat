package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record MonthlyCountQueryDto(Integer year, Integer month, Long count) {

	public static MonthlyCountQueryDto from(MonthlyCountSummary s) {
		return new MonthlyCountQueryDto(s.getY(), s.getM(), s.getC());
	}

}
