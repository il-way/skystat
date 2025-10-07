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

  public static String remarkTokens() {
    return "(RMK|REMARKS|REMARK|RMKS)";
  }

  private static String getRemarkRegex() {
    return remarkTokens() + "(?<rmk>[\\s\\S]+)";
  }
}
