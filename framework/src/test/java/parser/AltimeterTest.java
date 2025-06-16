package parser;

import org.junit.jupiter.api.Test;
import parser.metar.entry.AltimeterRegexParser;
import parser.shared.ReportParser;
import vo.metar.field.Altimeter;
import vo.unit.PressureUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AltimeterTest {

  ReportParser<Altimeter> parser = new AltimeterRegexParser();

  @Test
  public void 헥토파스칼_단위의_기압정보를_정상적으로_파싱한다() {
    String rawText = "METAR RKSI 030300Z 29008KT 260V320 9999 -RA BKN006 OVC045 11/10 Q1009 NOSIG=";
    Altimeter actual = parser.parse(rawText);

    Altimeter expected = Altimeter.of(1009, PressureUnit.HPA);
    assertEquals(expected, actual);
  }

  @Test
  public void 수은_단위의_기압정보를_헥토파스칼_단위로_정상_파싱한다() {
    String rawText = "KDEN 281539Z 33012G20KT 10SM FEW030 10/M02 A3002 RMK AO2 SLP200 T01000022=";
    Altimeter actual = parser.parse(rawText);

    Altimeter expected = Altimeter.of(30.02, PressureUnit.INHG);

    assertEquals(expected, actual);
  }

}
