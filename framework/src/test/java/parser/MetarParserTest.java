package parser;

import org.junit.jupiter.api.Test;
import parser.data.MetarTestData;
import parser.metar.MetarParser;
import vo.metar.Metar;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetarParserTest extends MetarTestData {

  private MetarParser parser = new MetarParser(YearMonth.of(2025, 5));

  @Test
  public void 메타_파싱에_성공해야한다() {
    Metar expected = testData.get(0);
    String rawText = expected.getRawText();

    Metar acutal = parser.parse(rawText);

    assertEquals(expected, acutal);
  }

}
