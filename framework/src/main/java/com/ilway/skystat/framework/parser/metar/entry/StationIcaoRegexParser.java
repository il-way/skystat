package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.StationIcaoRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.regex.Matcher;

public class StationIcaoRegexParser extends ReportRegexParser<String> {

  private final MetarField fieldType = MetarField.STATION_ICAO;
  private final String STATION_REGEX = StationIcaoRegexes.fullPattern();

  @Override
  public String parse(String rawText) {
    Matcher matcher = getMatcher(rawText, STATION_REGEX);

    if (!check(matcher)) {
      throw new IllegalArgumentException("Station not found in report: " + rawText);
    }

    String station = matcher.group(StationIcaoRegexes.STATION.getGroupName());

    return station;
  }

  @Override
  public MetarField getFieldType() {
    return fieldType;
  }

}