package com.ilway.skystat.framework.parser.shared;

import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.framework.parser.metar.regex.RemarkRegexes;
import com.ilway.skystat.framework.parser.metar.regex.TrendRegexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ReportRegexParser<T> implements ReportParser<T> {

  private static final Pattern REMARKS_TRUNCATOR =
    Pattern.compile(RemarkRegexes.fullPattern(), Pattern.MULTILINE);

  private static final Pattern TREND_TRUNCATOR =
    Pattern.compile(TrendRegexes.segmentPattern());

  private static final Pattern EQUALS_TAIL =
    Pattern.compile("=+$");

  private static final Pattern SPACE_SQUASH =
    Pattern.compile("\\s{2,}");

  public abstract MetarField getFieldType();

  protected Matcher getMatcher(String rawText, String regex) {
    return getMatcher(rawText, regex, true);
  }

  protected Matcher getMatcher(String rawText, String regex, boolean observationOnly) {
    String text = observationOnly ? preprocess(rawText) : rawText;
    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    return pattern.matcher(text);
  }

  protected boolean check(Matcher matcher) {
    return matcher.find();
  }

  protected String preprocess(String rawText) {
    return observationOnly(rawText);
  }

  protected String observationOnly(String rawText) {
    if (rawText == null || rawText.isBlank()) return "";

    String noRemarks = REMARKS_TRUNCATOR.matcher(rawText).replaceAll(" ");
    String noTrends = TREND_TRUNCATOR.matcher(noRemarks).replaceAll(" ");
    String noEquals = EQUALS_TAIL.matcher(noTrends).replaceAll("");
    return SPACE_SQUASH.matcher(noEquals).replaceAll(" ").trim();
  }

}