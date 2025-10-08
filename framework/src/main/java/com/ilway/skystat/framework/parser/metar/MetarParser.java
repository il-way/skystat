package com.ilway.skystat.framework.parser.metar;

import com.ilway.skystat.framework.exception.MetarParseException;
import com.ilway.skystat.framework.parser.metar.composite.CompositeRegexParser;
import com.ilway.skystat.framework.parser.metar.entry.*;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.Weathers;

import java.time.YearMonth;
import java.util.Map;

import static com.ilway.skystat.domain.vo.metar.MetarField.*;

public class MetarParser {

  private final CompositeRegexParser parser;

  public MetarParser(YearMonth yearMonth) {
    this.parser = new CompositeRegexParser();
    init(yearMonth);
  }

  public Metar parse(String rawText) {
    try {
      Map<MetarField, Object> map = parser.parse(rawText);
      return Metar.builder()
              .rawText(rawText)
              .stationIcao(require(map, STATION_ICAO))
              .reportType(require(map, REPORT_TYPE))
              .observationTime(require(map, OBSERVATION_TIME))
              .wind(require(map, WIND))
              .visibility(require(map, VISIBILITY))
              .temperature(require(map, TEMPERATURE))
              .dewPoint(require(map, DEW_POINT))
              .altimeter(require(map, ALTIMETER))
              .weathers((Weathers) map.getOrDefault(WEATHERS, Weathers.ofEmpty()))
              .clouds((Clouds) map.getOrDefault(CLOUDS, Clouds.ofEmpty()))
              .remarks((String) map.getOrDefault(REMARKS, ""))
              .build();
    }
    catch (ClassCastException | NullPointerException e) {
      throw new MetarParseException("Failed to build Metar fromInclusive raw: " + rawText, e);
    }
    catch (IllegalArgumentException e) {
      throw new MetarParseException("Failed to parse Metar because " + e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T require(Map<MetarField, Object> m, MetarField field) {
    Object v = m.get(field);
    if (v == null) {
      throw new MetarParseException("Missing required field: " + field);
    }
    return (T) v;
  }

  public void setYearMonth(YearMonth ym) {
    parser.setYearMonth(ym);
  }

  /** 현재 사용 중인 연/월을 조회 */
  public YearMonth getYearMonth() {
    return parser.getYearMonth();
  }

  private void init(YearMonth yearMonth) {
    var stationIcaoRegexParser = new StationIcaoRegexParser();
    var reportTypeRegexParser = new ReportTypeRegexParser();
    var observationTimeRegexParser = new ObservationTimeRegexParser(yearMonth);
    var windRegexParser = new WindRegexParser();
    var visibilityRegexParser = new VisibilityRegexParser();
    var temperatureRegexParser = new TemperatureRegexParser();
    var dewPointRegexParser = new DewPointRegexParser();
    var altimeterRegexParser = new AltimeterRegexParser();
    var weatherGroupRegexParser = new WeathersRegexParser();
    var cloudGroupRegexParser = new CloudsRegexParser();
    var remarkRegexParser = new RemarkRegexParser();

    parser.add(stationIcaoRegexParser);
    parser.add(reportTypeRegexParser);
    parser.add(observationTimeRegexParser);
    parser.add(windRegexParser);
    parser.add(visibilityRegexParser);
    parser.add(temperatureRegexParser);
    parser.add(dewPointRegexParser);
    parser.add(altimeterRegexParser);
    parser.add(weatherGroupRegexParser);
    parser.add(cloudGroupRegexParser);
    parser.add(remarkRegexParser);
  }

}
