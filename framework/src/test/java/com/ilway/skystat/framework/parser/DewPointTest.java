package com.ilway.skystat.framework.parser;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.DewPointRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;
import com.ilway.skystat.domain.vo.weather.Temperature;
import com.ilway.skystat.domain.vo.unit.TemperatureUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DewPointTest {

  ReportParser<Temperature> parser = new DewPointRegexParser();

  @Test
  public void 양의_온도쌍을_가진_메타_파싱에_성공해야한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    Temperature t = parser.parse(rawText);
    Temperature expected = Temperature.of(12, TemperatureUnit.CELSIUS);

    assertEquals(expected, t);
  }

  @Test
  public void 양의_온도와_음의_이슬점을_갖는_메타_파싱에_성공해야한다() {
    String rawText = "KDEN 281539Z 33012G20KT 10SM FEW030 10/M02 A3002 RMK AO2 SLP200 T01000022=";

    Temperature t = parser.parse(rawText);
    Temperature expected = Temperature.of(-2, TemperatureUnit.CELSIUS);

    assertEquals(expected, t);
  }

  @Test
  public void 음의_온도와_양의_이슬점을_갖는_메타_파싱에_성공해야한다() {
    String rawText = "KJFK 031752Z 12009KT 3SM -RA BR OVC011 M17/16 A3012 RMK AO2 SLP197 P0004 T01670156=";

    Temperature t = parser.parse(rawText);
    Temperature expected = Temperature.of(16, TemperatureUnit.CELSIUS);

    assertEquals(expected, t);
  }

  @Test
  public void 음의_온도쌍을_갖는_메타_파싱에_성공해야한다() {
    String rawText = "KJFK 051830Z 18012G23KT 10SM FEW035 SCT120 M22/M14 A3002 RMK AO2 SLP157 T02220139=";

    Temperature t = parser.parse(rawText);
    Temperature expected = Temperature.of(-14, TemperatureUnit.CELSIUS);

    assertEquals(expected, t);
  }

  @Test
  public void 온도정보가_누락되면_예외가_발생한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 /12 A2995 RMK AO2 SLP142 T01780122=";

    assertThrows(IllegalArgumentException.class, () -> parser.parse(rawText));
  }

  @Test
  public void 노점정보가_누락되면_예외가_발생한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 16/ A2995 RMK AO2 SLP142 T01780122=";

    assertThrows(IllegalArgumentException.class, () -> parser.parse(rawText));
  }

  @Test
  public void 온도쌍_정보가_누락되면_예외가_발생한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 A2995 RMK AO2 SLP142 T01780122=";

    assertThrows(IllegalArgumentException.class, () -> parser.parse(rawText));
  }

}
