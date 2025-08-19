package com.ilway.skystat.application.statistic.data;

import com.ilway.skystat.domain.vo.weather.*;
import lombok.Getter;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.metar.ReportType.METAR;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.HPA;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static com.ilway.skystat.domain.vo.weather.type.WeatherInensity.MODERATE;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.BR;

@Getter
public class RKSI202401010200 {

	/*──────── 원문 (2024-01-01) ────────*/
	private final String rawText =
		"RKSI 010200Z 14005KT 110V170 5000 BR NSC 05/01 Q1030 NOSIG";

	/*──────── Metar 객체 ────────*/
	protected final Metar testData =
		Metar.builder()
			.rawText(rawText)
			.stationIcao("RKSI")
			.reportType(METAR)
			.observationTime(ofLenientUtc(2024, 1, 1, 2, 0))      // 010200Z
			.wind(Wind.of(WindDirection.fixed(140), 5, 0, KT))   // 140° 5 kt
			.visibility(Visibility.of(5000, METERS))              // 5 000 m
			.temperature(Temperature.of(5, CELSIUS))              // 05 °C
			.dewPoint   (Temperature.of(1, CELSIUS))              // 01 °C
			.altimeter  (Altimeter.of(1030, HPA))                 // Q1030
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, null, List.of(BR))           // BR
			)))
			.cloudGroup(CloudGroup.of(List.of()))                 // NSC
			.remarks("NOSIG")
			.build();

}
