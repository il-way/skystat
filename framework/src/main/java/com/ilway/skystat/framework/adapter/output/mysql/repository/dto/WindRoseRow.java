package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public interface WindRoseRow {
	Integer getYear();
	Integer getMonth();
	Integer getDirOrder();
	String  getDirLabel();
	Integer getSpeedOrder();
	String  getSpeedLabel();
	Long    getFreq();
	Long    getFixedSample();
}
