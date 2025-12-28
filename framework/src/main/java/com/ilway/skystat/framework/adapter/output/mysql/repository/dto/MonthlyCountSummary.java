package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public interface MonthlyCountSummary {
	Integer getY(); // 년
	Integer getM(); // 월
	Long getC();    // 개수
}
