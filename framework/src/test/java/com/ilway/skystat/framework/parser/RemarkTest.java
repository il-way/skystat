package com.ilway.skystat.framework.parser;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.RemarkRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemarkTest {

  ReportParser<String> parser = new RemarkRegexParser();

  @Test
  public void 리마크가_존재하는_메타의_파싱에_성공해야한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    String actual = parser.parse(rawText);
    String expected = "AO2 SLP142 T01780122=";

    assertEquals(expected, actual);
  }

  @Test
  public void 리마크가_존재하지_않는_메타는_공백문자열이_파싱된다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995";

    String actual = parser.parse(rawText);
    String expected = "";

    assertEquals(expected, actual);
  }

}
