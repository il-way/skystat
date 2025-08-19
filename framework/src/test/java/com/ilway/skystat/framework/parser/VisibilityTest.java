package com.ilway.skystat.framework.parser;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.VisibilityRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;
import com.ilway.skystat.domain.vo.weather.Visibility;
import com.ilway.skystat.domain.vo.unit.LengthUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VisibilityTest {

  ReportParser<Visibility> parser = new VisibilityRegexParser();

  @Test
  public void 시정이_관측되지_않은_경우_예외가_발생한다() {
    String rawText = "KSFO 030953Z 29008KT FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    assertThrows(IllegalArgumentException.class, () -> {
      parser.parse(rawText);
    });
  }

  @Test
  public void P6SM이_관측된_경우_시정값이_6마일이다() {
    // given
    String rawText = "KSFO 030953Z 29008KT P6SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    // when, then
    Visibility visibility = parser.parse(rawText);
    Visibility expected = Visibility.of(6, LengthUnit.MILE);

    assertEquals(expected, visibility);
  }

  @Test
  public void CAVOK이_관측된_경우_시정값이_9999미터이다() {
    // given
    String rawText = "METAR RKPK 030300Z 21008KT CAVOK 16/12 Q1007 RMK CIG070 SLP076 8/72/ 9/35/=";

    // when
    Visibility visibility = parser.parse(rawText);
    Visibility expected = Visibility.of(9999, LengthUnit.METERS);

    assertEquals(expected, visibility);
  }

  @Test
  public void 분수형태의_마일시정이_관측된_경우_소수로_변환된_시정객체를_반환한다() {
    // given
    String rawText = "SPECI RKJK 010647Z VRB11KT 3/4SM -RA BR OVC004 15/14 A2969 RMK AO2A VIS 1 3/4V3 RAB0555E25RAB42E43DZB43E46RAB46 CIG 004V006 CIG 003 RWY18 SLPNO $=";

    // when
    Visibility visibility = parser.parse(rawText);
    Visibility expected = Visibility.of(0.75, LengthUnit.MILE);

    assertEquals(expected, visibility);
  }

  @Test
  public void 대분수형태의_마일시정이_관측된_경우_소수로로_변환된_시정객체를_반환한다() {
    // given
    String rawText = "SPECI RKJK 010647Z VRB11KT 1 3/4SM -RA BR OVC004 15/14 A2969 RMK AO2A VIS 1 3/4V3 RAB0555E25RAB42E43DZB43E46RAB46 CIG 004V006 CIG 003 RWY18 SLPNO $=";

    // when
    Visibility visibility = parser.parse(rawText);
    Visibility expected = Visibility.of(1.75, LengthUnit.MILE);

    assertEquals(expected, visibility);
  }

}
