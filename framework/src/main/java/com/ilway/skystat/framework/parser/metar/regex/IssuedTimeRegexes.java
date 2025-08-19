package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssuedTimeRegexes {

  YEAR("year", getYearRegex()),
  MONTH("month", getMonthRegex()),
  DAY("day", getDayRegex()),
  HOUR("hour", getHourRegex()),
  MINUTE("minute", getMinuteRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(%s)-(%s)-(%s) (%s):(%s)",
            getYearRegex(),
            getMonthRegex(),
            getDayRegex(),
            getHourRegex(),
            getMinuteRegex());
  }

  private static String getYearRegex() {
    return "(?<year>\\d{4})";
  }

  private static String getMonthRegex() {
    return "(?<month>\\d{2})";
  }

  private static String getDayRegex() {
    return "(?<day>\\d{2})";
  }

  private static String getHourRegex() {
    return "(?<hour>\\d{2})";
  }

  private static String getMinuteRegex() {
    return "(?<minute>\\d{2})";
  }

}
