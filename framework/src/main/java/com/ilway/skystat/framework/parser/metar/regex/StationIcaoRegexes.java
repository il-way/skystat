package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StationIcaoRegexes {
  STATION("station", getStationRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(?:^|\\s|[\\p{Punct}])(%s)\\s(%s)",
            getStationRegex(),
            ObservationTimeRegexes.fullPattern()
    );
  }

  private static String getStationRegex() {
    return "(?<station>[A-Z]{4})";
  }
}
