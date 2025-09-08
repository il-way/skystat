package com.ilway.skystat.framework.adapter.output.mysql.mapper;

import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.framework.adapter.output.mysql.data.CloudData;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.data.WeatherData;

public class MetarMySQLMapper {

	public static Cloud cloudDataToDomain(CloudData cloudData) {
		return Cloud.of(
			CloudCoverage.valueOf(cloudData.getCoverage()),
			cloudData.getAltitude(),
			CloudType.valueOf(cloudData.getCloudType())
		);
	}

	public static CloudData cloudDomainToData(Cloud cloud) {
		return null;
	}

	public static Weather weatherDataToDomain(WeatherData weatherData) {
		return null;
	}

	public static WeatherData weatherDomainToData(Weather weather) {
		return null;
	}

	public static Metar metarDataToDomain(MetarData metarData) {
		return null;
	}

	public static MetarData metarDomainToData(Metar metar) {
		return null;
	}

}
