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
public class RKSI202401010000 {

	/*──────── 원문 (2024-01-01) ────────*/
	private final String rawText =
		"RKSI 010000Z 07002KT 3500 BR NSC 01/00 Q1029 NOSIG";

	/*──────── Metar 객체 ────────*/
	protected final Metar testData =
		Metar.builder()
			.rawText(rawText)
			.stationIcao("RKSI")
			.reportType(METAR)
			.observationTime(ofLenientUtc(2024, 1, 1, 0, 0))
			.wind(Wind.of(WindDirection.fixed(70), 2, 0, KT))
			.visibility (Visibility.of(3500, METERS))
			.temperature(Temperature.of(1,  CELSIUS))
			.dewPoint   (Temperature.of(0,  CELSIUS))
			.altimeter  (Altimeter.of(1029, HPA))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, null, List.of(BR))
			)))
			.cloudGroup(CloudGroup.of(List.of()))   // NSC
			.remarks("NOSIG")
			.build();

}
