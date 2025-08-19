package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.IssuedTimeRegexes;
import com.ilway.skystat.framework.parser.shared.ReportParser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssuedTimeRegexParser implements ReportParser<ZonedDateTime> {

  private static final String ISSUED_TIME_REGEX = IssuedTimeRegexes.fullPattern();

  @Override
  public ZonedDateTime parse(String rawText) {
    Pattern pattern = Pattern.compile(ISSUED_TIME_REGEX);
    Matcher matcher = pattern.matcher(rawText);

    if (!matcher.find()) {
      throw new IllegalArgumentException("Issued time not found in mesonet report: " + rawText);
    }

    int year = Integer.parseInt(matcher.group(IssuedTimeRegexes.YEAR.getGroupName()));
    int month = Integer.parseInt(matcher.group(IssuedTimeRegexes.MONTH.getGroupName()));
    int day = Integer.parseInt(matcher.group(IssuedTimeRegexes.DAY.getGroupName()));
    int hour = Integer.parseInt(matcher.group(IssuedTimeRegexes.HOUR.getGroupName()));
    int minute = Integer.parseInt(matcher.group(IssuedTimeRegexes.MINUTE.getGroupName()));

    LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);

    return ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
  }

}