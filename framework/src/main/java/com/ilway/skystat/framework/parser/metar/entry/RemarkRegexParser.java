package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.RemarkRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.regex.Matcher;

public class RemarkRegexParser extends ReportRegexParser<String> {

  private static final MetarField FIELD_TYPE= MetarField.REMARKS;
  private static final String REMARK_REGEX = RemarkRegexes.fullPattern();

  @Override
  public String parse(String rawText) {
    Matcher matcher = getMatcher(rawText, REMARK_REGEX, false);

    if (!check(matcher)) {
      return "";
    }

    for (RemarkRegexes type: RemarkRegexes.values()) {
      String match = matcher.group(type.getGroupName());

      if (match == null || match.isEmpty()) {
        continue;
      }

      return match.trim();
    }

    return "";
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}
