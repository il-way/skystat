package parser.data;

import vo.metar.Metar;
import vo.metar.field.*;
import vo.metar.type.CloudCoverage;
import vo.metar.type.CloudType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static vo.metar.type.ReportType.METAR;
import static vo.unit.LengthUnit.MILE;
import static vo.unit.PressureUnit.INHG;
import static vo.unit.SpeedUnit.KT;
import static vo.unit.TemperatureUnit.CELSIUS;

public class MetarTestData {

  protected List<Metar> testData = List.of(
      Metar.builder()
          .rawText("KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=")
          .stationIcao("KSFO")
          .reportType(METAR)
          .observationTime(generateTime("030953"))
          .reportTime(generateTime("031000"))
          .wind(generateWind(290,8,0))
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

  private static ZonedDateTime generateTime(String ddhhmm) {
    return ZonedDateTime.of(
        2025,
        5,
        Integer.parseInt(ddhhmm.substring(0, 2)),
        Integer.parseInt(ddhhmm.substring(2, 4)),
        Integer.parseInt(ddhhmm.substring(4, 6)),
        0,
        0,
        ZoneId.of("UTC"));
  }

  private static Wind generateWind(double dir, double speed, double gust) {
    return Wind.of(WindDirection.fixed(dir), speed, gust, KT);
  }

}
