package com.ilway.skystat.framework.parser.data;

import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.*;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;

import java.util.List;

import static com.ilway.skystat.domain.service.TimeOperation.ofLenientUtc;
import static com.ilway.skystat.domain.vo.metar.ReportType.METAR;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.MILE;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.INHG;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static com.ilway.skystat.domain.vo.weather.WindDirection.fixed;

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
