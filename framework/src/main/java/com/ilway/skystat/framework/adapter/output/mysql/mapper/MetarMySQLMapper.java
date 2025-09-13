package com.ilway.skystat.framework.adapter.output.mysql.mapper;

import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.metar.ReportType;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.WindDirection;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.framework.adapter.output.mysql.data.CloudData;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.data.WeatherData;

public class MetarMySQLMapper {

	public static Cloud cloudDataToDomain(CloudData cloudData) {
		return Cloud.of(
			cloudData.getCoverage(),
			cloudData.getAltitude(),
			cloudData.getCloudType()
		);
	}

	public static CloudData cloudDomainToData(Cloud cloud) {
		return CloudData.builder()
			.coverage(cloud.getCoverage())
			.altitude(cloud.getAltitudeOptional().orElse(null))
			.cloudType(cloud.getType())
			.build();
	}

	public static Weather weatherDataToDomain(WeatherData weatherData) {
		return Weather.of(
			weatherData.getIntensity(),
			weatherData.getDescriptors(),
			weatherData.getPhenomena()
		);
	}

	public static WeatherData weatherDomainToData(Weather weather) {
		return WeatherData.builder()
			       .intensity(weather.getIntensity())
			       .descriptors(weather.getDescriptors())
			       .phenomena(weather.getPhenomena())
			       .build();
	}

	public static Metar metarDataToDomain(MetarData metarData) {
		WindDirectionType windDirectionType = metarData.getWindDirectionType();
		WindDirection.of(windDirectionType, metarData.getWindDirection());

		return Metar.builder()
			       .rawText(metarData.getRawText())
			       .stationIcao(metarData.getStationIcao())
			       .reportType(metarData.getReportType())
			       .wind()
			       .build();
	}

	public static MetarData metarDomainToData(Metar metar) {
		return null;
	}


}
