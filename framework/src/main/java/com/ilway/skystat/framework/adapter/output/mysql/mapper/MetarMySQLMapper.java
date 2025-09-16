package com.ilway.skystat.framework.adapter.output.mysql.mapper;

import com.ilway.skystat.domain.service.CloudOperation;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.metar.ReportType;
import com.ilway.skystat.domain.vo.unit.TemperatureUnit;
import com.ilway.skystat.domain.vo.weather.*;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.framework.adapter.output.mysql.data.CloudData;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.data.WeatherData;

import java.util.List;
import java.util.stream.Collectors;

import static com.ilway.skystat.domain.vo.metar.ReportType.AUTO;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;

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
		return Metar.builder()
			       .rawText(metarData.getRawText())
			       .stationIcao(metarData.getStationIcao())
			       .reportType(metarData.getReportType())
			       .observationTime(metarData.getObservationTime())
			       .reportTime(metarData.getReportTime())
			       .wind(metarDataToWindDomain(metarData))
			       .visibility(Visibility.of(metarData.getVisibility(), metarData.getVisibilityUnit()))
			       .temperature(Temperature.of(metarData.getTemperature(), CELSIUS))
			       .dewPoint(Temperature.of(metarData.getDewPoint(), CELSIUS))
			       .altimeter(Altimeter.of(metarData.getAltimeter(), metarData.getAltimeterUnit()))
			       .weatherGroup(WeatherGroup.of(
				       metarData.getWeatherList().stream()
					       .map(MetarMySQLMapper::weatherDataToDomain)
					       .toList()
			       ))
			       .cloudGroup(CloudGroup.of(
				       metarData.getCloudList().stream()
					       .map(MetarMySQLMapper::cloudDataToDomain)
					       .toList()
			       ))
			       .remarks(metarData.getRemarks())
			       .build();
	}

	public static MetarData metarDomainToData(Metar metar) {
		return MetarData.builder()
			.stationIcao(metar.getStationIcao())
			.observationTime(metar.getObservationTime())
			.reportTime(metar.getReportTime())
			.reportType(metar.getReportType())
			.isAuto(metar.getReportType().equals(AUTO))
			.windDirectionType(metar.getWind().getDirection().getType())
			.windUnit(metar.getWind().getUnit())
			.windDirection(metar.getWind().getDirection().getDegreeOptional().orElse(null))
			.windSpeed(metar.getWind().getSpeed())
			.windGust(metar.getWind().getGusts())
			.windVariableFrom(null)
			.windVariableTo(null)
			.visibilityUnit(metar.getVisibility().getUnit())
			.visibility(metar.getVisibility().getValue())
			.temperature(metar.getTemperature().getUnit()
				             .toBase(metar.getTemperature().getValue()))
			.dewPoint(metar.getDewPoint().getUnit()
				          .toBase(metar.getDewPoint().getValue()))
			.altimeterUnit(metar.getAltimeter().getUnit())
			.altimeter(metar.getAltimeter().getValue())
			.ceiling(CloudOperation.getLowestCeiling(metar.getCloudGroup()))
			.cloudLayerCount(metar.getCloudGroup().size())
			.weatherPresent(metar.getWeatherGroup().size() > 0)
			.remarks(metar.getRemarks())
			.rawText(metar.getRawText())
			.cloudList(metar.getCloudGroup().getCloudList().stream()
				           .map(MetarMySQLMapper::cloudDomainToData)
				           .collect(Collectors.toSet()))
			.weatherList(metar.getWeatherGroup().getWeatherList().stream()
				             .map(MetarMySQLMapper::weatherDomainToData)
				             .collect(Collectors.toSet()))
			.build();
	}

	private static Wind metarDataToWindDomain(MetarData metarData) {
		WindDirectionType windDirectionType = metarData.getWindDirectionType();
		WindDirection windDirection = WindDirection.of(windDirectionType, metarData.getWindDirection());
		return Wind.of(windDirection, metarData.getWindSpeed(), metarData.getWindGust(), metarData.getWindUnit());
	}

}
