package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum WindRegexes {
  DIRECTION("direction", getDirectionRegex()),
  SPEED("speed", getSpeedRegex()),
  GUSTS("gusts", getGustsRegex()),
  UNIT("unit", getUnitRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(?:^|\\s)(%s)(%s)(%s)?(%s)(?=(?:\\s|$))",
            getDirectionRegex(),
            getSpeedRegex(),
            getGustsRegex(),
            getUnitRegex()
    );
  }

  private static String getDirectionRegex() {
    return "(?<direction>\\d{3}|VRB)";
  }

  private static String getSpeedRegex() {
    return "(?<speed>P?\\d{2})";
  }

  private static String getGustsRegex() {
    return "G(?<gusts>P?\\d{2})";
  }

  private static String getUnitRegex() {
    return Arrays.stream(SpeedUnit.values())
            .map(Enum::name)
            .collect(Collectors.joining("|", "(?<unit>", ")"));
  }
}
