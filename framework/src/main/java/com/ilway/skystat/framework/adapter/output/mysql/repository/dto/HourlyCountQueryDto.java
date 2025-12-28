package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public record HourlyCountQueryDto(int year, int month, int hour, Long count) {

	public static HourlyCountQueryDto from(HourlyCountSummary s) {
		return new HourlyCountQueryDto(s.getY(), s.getM(), s.getH(), s.getC());
	}

}
