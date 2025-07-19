package parser.data;

import vo.metar.Metar;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;
import vo.weather.*;

import java.util.List;

import static service.TimeOperation.ofLenientUtc;
import static vo.metar.ReportType.METAR;
import static vo.unit.LengthUnit.MILE;
import static vo.unit.PressureUnit.INHG;
import static vo.unit.SpeedUnit.KT;
import static vo.unit.TemperatureUnit.CELSIUS;
import static vo.weather.WindDirection.fixed;

public class MetarTestData {

  protected List<Metar> testData = List.of(
      Metar.builder()
          .rawText("KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=")
          .stationIcao("KSFO")
          .reportType(METAR)
          .observationTime(ofLenientUtc(2025, 5, 3, 9, 53))
          .wind(Wind.of(fixed(290), 8, 0, KT))
          .visibility(Visibility.of(10, MILE))
          .temperature(Temperature.of(18,CELSIUS))
          .dewPoint(Temperature.of(12,CELSIUS))
          .altimeter(Altimeter.of(29.95, INHG))
          .weatherGroup(WeatherGroup.of(List.of()))
          .cloudGroup(CloudGroup.of(
              List.of(
                  Cloud.of(CloudCoverage.FEW, 2500, CloudType.NONE),
                  Cloud.of(CloudCoverage.SCT, 25000, CloudType.NONE)
              )
          ))
          .remarks("AO2 SLP142 T01780122=")
          .build()
  );

}
