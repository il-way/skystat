package com.ilway.skystat.framework.parser;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.StationIcaoRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StationTest {

  ReportParser<String> parser = new StationIcaoRegexParser();

  @Test
  public void 스테이션_파싱에_성공해야한다() {
    String rawText = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";

    String expected = "RKSI";
    String actual = parser.parse(rawText);

    assertEquals(expected, actual);
  }

  @Test
  public void 관측시간이_누락된_메타에선_스테이션_파싱에_실패해야한다() {
    String rawText = "RKSI 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";

    assertThrows(IllegalArgumentException.class, () -> parser.parse(rawText));
  }

  @Test
  public void 스테이션이_누락된_메타에선_스테이션_파싱에_실패해야한다() {
    String rawText = "010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";

    assertThrows(IllegalArgumentException.class, () -> parser.parse(rawText));
  }

  @Test
  public void 정상적인_메타에선_앞선_문자와_무관하게_스테이션_파싱에_실패해야한다() {
    String rawText = "SPECI RKJK 010647Z 27011KT 1 3/4SM -RA BR OVC004 15/14 A2969 RMK AO2A VIS 1 3/4V3 RAB0555E25RAB42E43DZB43E46RAB46 CIG 004V006 CIG 003 RWY18 SLPNO $=";

    String expected = "RKJK";
    String actual = parser.parse(rawText);

    assertEquals(expected, actual);
  }

}
