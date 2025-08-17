package windrose.data;

import lombok.Getter;
import vo.metar.Metar;
import vo.weather.*;

import java.util.List;

import static service.TimeOperation.ofLenientUtc;
import static vo.metar.ReportType.METAR;
import static vo.unit.LengthUnit.METERS;
import static vo.unit.PressureUnit.HPA;
import static vo.unit.SpeedUnit.KT;
import static vo.unit.TemperatureUnit.CELSIUS;
import static vo.weather.type.WeatherInensity.MODERATE;
import static vo.weather.type.WeatherPhenomenon.BR;

@Getter
public class RKSI202401010030 {

	/*──────── 원문 (2024-01-01) ────────*/
	private final String rawText =
		"RKSI 010030Z 10004KT 030V150 4000 BR NSC 03/01 Q1030 NOSIG";

	/*──────── Metar 객체 ────────*/
	protected final Metar testData =
		Metar.builder()
			.rawText(rawText)
			.stationIcao("RKSI")
			.reportType(METAR)
			.observationTime(ofLenientUtc(2024, 1, 1, 0, 30))     // 010030Z
			.wind(Wind.of(WindDirection.fixed(100), 4, 0, KT))    // 100° 4 kt
			.visibility(Visibility.of(4000, METERS))              // 4 000 m
			.temperature(Temperature.of(3,  CELSIUS))             // 03 °C
			.dewPoint   (Temperature.of(1,  CELSIUS))             // 01 °C
			.altimeter  (Altimeter.of(1030, HPA))                 // Q1030
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, null, List.of(BR))           // BR
			)))
			.cloudGroup(CloudGroup.of(List.of()))                 // NSC
			.remarks("NOSIG")
			.build();

}
