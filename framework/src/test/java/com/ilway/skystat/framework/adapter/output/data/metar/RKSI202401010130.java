package com.ilway.skystat.framework.adapter.output.data.metar;

import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.*;
import lombok.Getter;

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
public class RKSI202401010130 {

	/*──────── 원문 (2024-01-01) ────────*/
	private final String rawText =
		"RKSI 010130Z 12004KT 080V160 5000 BR NSC 04/01 Q1030 NOSIG";

	/*──────── Metar 객체 ────────*/
	protected final Metar testData =
		Metar.builder()
			.rawText(rawText)
			.stationIcao("RKSI")
			.reportType(METAR)
			.observationTime(ofLenientUtc(2024, 1, 1, 1, 30))     // 010130Z
			.wind(Wind.of(WindDirection.fixed(120), 4, 0, KT))    // 120° 4 kt
			.visibility(Visibility.of(5000, METERS))              // 5 000 m
			.temperature(Temperature.of(4, CELSIUS))              // 04 °C
			.dewPoint   (Temperature.of(1, CELSIUS))              // 01 °C
			.altimeter  (Altimeter.of(1030, HPA))                 // Q1030
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, null, List.of(BR))           // BR
			)))
			.cloudGroup(CloudGroup.of(List.of()))                 // NSC
			.remarks("NOSIG")
			.build();

}
