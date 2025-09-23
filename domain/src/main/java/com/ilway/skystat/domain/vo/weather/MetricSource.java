package com.ilway.skystat.domain.vo.weather;

public interface MetricSource {

	Visibility getVisibility();
	Wind getWind();
	Altimeter getAltimeter();
	Clouds getClouds();

}
