package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.ReportTypeRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.metar.ReportType;

import java.util.regex.Matcher;

public class ReportTypeRegexParser extends ReportRegexParser<ReportType> {

  private static final MetarField FIELD_TYPE= MetarField.REPORT_TYPE;
  private static final String REPORT_TYPE_REGEX = ReportTypeRegexes.fullPattern();

  @Override
  public ReportType parse(String rawText) {
    Matcher matcher = getMatcher(rawText, REPORT_TYPE_REGEX);

    if (!check(matcher)) {
      return ReportType.METAR;
    }

    String reportType = matcher.group(ReportTypeRegexes.TYPE.getGroupName());

    return ReportType.valueOf(reportType);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}