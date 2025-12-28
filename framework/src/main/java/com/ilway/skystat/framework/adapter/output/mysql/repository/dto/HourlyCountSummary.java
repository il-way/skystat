package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public interface HourlyCountSummary {
	Integer getY(); // 년
	Integer getM(); // 월
	Integer getH(); // 시 (추가됨)
	Long getC();    // 개수
}
