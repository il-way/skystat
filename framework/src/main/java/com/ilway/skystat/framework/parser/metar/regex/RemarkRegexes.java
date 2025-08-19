package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RemarkRegexes {
  RMK("rmk", getRemarkRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(?:^|\\s)(%s)(?=(?:\\s|$))", getRemarkRegex());
  }

  private static String getRemarkRegex() {
    return "RMK(?<rmk>[\\s\\S]+)";
  }
}
