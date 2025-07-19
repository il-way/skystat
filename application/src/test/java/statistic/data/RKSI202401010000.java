package statistic.data;

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
