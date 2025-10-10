package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

public interface WindRoseRow {
	Integer getY();
	Integer getM();
	Integer getDirOrder();
	String  getDirLabel();
	Integer getSpeedOrder();
	String  getSpeedLabel();
	Long    getFreq();
}
