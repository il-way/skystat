package com.ilway.skystat.framework.adapter.output.mysql.mapper;

import com.ilway.skystat.domain.service.CloudOperation;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.*;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.framework.adapter.output.mysql.data.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.ilway.skystat.domain.vo.metar.ReportType.AUTO;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static java.util.stream.Collectors.joining;

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
			weatherData.getDescriptors().stream()
				.map(WeatherDescriptorData::getDescriptor)
				.toList(),
			weatherData.getPhenomena().stream()
				.map(WeatherPhenomenonData::getPhenomenon)
				.toList()
		);
	}

	public static WeatherData weatherDomainToData(Weather weather) {
		WeatherData wd = WeatherData.builder()
			                    .intensity(weather.getIntensity())
			                    .descriptors(weather.getDescriptors().stream()
				                                 .map(MetarMySQLMapper::weatherDescriptorDomainToData)
				                                 .collect(Collectors.toList())
			                    )
			                    .phenomena(weather.getPhenomena().stream()
				                               .map(MetarMySQLMapper::weatherPhenomenonDomainToData)
				                               .collect(Collectors.toList()))
			                    .rawCode(buildWeatherRawCode(weather))
			                    .build();

		wd.getDescriptors().forEach(d -> d.setWeather(wd));
		wd.getPhenomena().forEach(p -> p.setWeather(wd));

		return wd;
	}


	public static WeatherDescriptor weatherDescriptorDataToDomain(WeatherDescriptorData weatherDescriptorData) {
		return weatherDescriptorData.getDescriptor();
	}

	public static WeatherDescriptorData weatherDescriptorDomainToData(WeatherDescriptor weatherDescriptor) {
		return WeatherDescriptorData.builder()
			       .descriptor(weatherDescriptor)
			       .build();
	}

	public static WeatherPhenomenon weatherPhenomenonDataToDomain(WeatherPhenomenonData weatherPhenomenonData) {
		return weatherPhenomenonData.getPhenomenon();
	}

	public static WeatherPhenomenonData weatherPhenomenonDomainToData(WeatherPhenomenon weatherPhenomenon) {
		return WeatherPhenomenonData.builder()
			       .phenomenon(weatherPhenomenon)
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
			       .weathers(Weathers.of(
				       metarData.getWeathers().stream()
					       .map(MetarMySQLMapper::weatherDataToDomain)
					       .toList()
			       ))
			       .clouds(Clouds.of(
				       metarData.getClouds().stream()
					       .map(MetarMySQLMapper::cloudDataToDomain)
					       .toList()
			       ))
			       .remarks(metarData.getRemarks())
			       .build();
	}

	public static MetarData metarDomainToData(Metar metar) {
		MetarData md = MetarData.builder()
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
			                  .ceiling(CloudOperation.getLowestCeiling(metar.getClouds()))
			                  .cloudLayerCount(metar.getClouds().size())
			                  .weatherPresent(metar.getWeathers().size() > 0)
			                  .remarks(metar.getRemarks())
			                  .rawText(metar.getRawText())
			                  .clouds(metar.getClouds().getClouds().stream()
				                          .map(MetarMySQLMapper::cloudDomainToData)
				                          .collect(Collectors.toSet()))
			                  .weathers(metar.getWeathers().getWeathers().stream()
				                            .map(MetarMySQLMapper::weatherDomainToData)
				                            .collect(Collectors.toSet()))
			                  .build();

		md.getClouds().forEach(c -> c.setMetar(md));
		md.getWeathers().forEach(w -> {
			w.setMetar(md);
			w.getDescriptors().forEach(d -> d.setWeather(w));
			w.getPhenomena().forEach(p -> p.setWeather(w));
		});

		return md;
	}

	private static Wind metarDataToWindDomain(MetarData metarData) {
		WindDirectionType windDirectionType = metarData.getWindDirectionType();
		WindDirection windDirection = WindDirection.of(windDirectionType, metarData.getWindDirection());
		return Wind.of(windDirection, metarData.getWindSpeed(), metarData.getWindGust(), metarData.getWindUnit());
	}

	private static String buildWeatherRawCode(Weather w) {
		String intensity = w.getIntensity().getSymbol();
		String desc = w.getDescriptors().stream().map(Enum::name).collect(joining());
		String phenomena = w.getPhenomena().stream().map(Enum::name).collect(joining());

		return (intensity + desc + phenomena).toUpperCase();
	}

}
