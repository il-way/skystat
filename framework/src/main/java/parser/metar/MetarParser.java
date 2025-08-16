package parser.metar;

import exception.MetarParseException;
import parser.metar.composite.CompositeRegexParser;
import parser.metar.entry.*;
import vo.metar.Metar;
import vo.metar.MetarField;
import vo.weather.CloudGroup;
import vo.weather.WeatherGroup;

import static vo.metar.MetarField.*;

import java.time.YearMonth;
import java.util.Map;

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
              .weatherGroup((WeatherGroup) map.getOrDefault(WEATHER_GROUP, WeatherGroup.ofEmpty()))
              .cloudGroup((CloudGroup) map.getOrDefault(CLOUD_GROUP, CloudGroup.ofEmpty()))
              .remarks((String) map.getOrDefault(REMARKS, ""))
              .build();
    }
    catch (ClassCastException | NullPointerException e) {
      throw new MetarParseException("Failed to build Metar from raw: " + rawText, e);
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
    var weatherGroupRegexParser = new WeatherGroupRegexParser();
    var cloudGroupRegexParser = new CloudGroupRegexParser();
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
