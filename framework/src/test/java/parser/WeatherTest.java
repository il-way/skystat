package parser;

import org.junit.jupiter.api.Test;
import parser.metar.entry.WeatherGroupRegexParser;
import parser.metar.entry.WeatherRegexParser;
import parser.shared.ReportParser;
import vo.weather.Weather;
import vo.weather.WeatherGroup;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherInensity;
import vo.weather.type.WeatherPhenomenon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;

public class WeatherTest {

  ReportParser<Weather> wxParser = new WeatherRegexParser();
  ReportParser<WeatherGroup> wxGroupParser = new WeatherGroupRegexParser();

  @Test
  public void 관측된_기상현상이_없는_경우_날씨파서는_NULL을_반환한다() {
    // given
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    Weather weather = wxParser.parse(rawText);

    assertNull(null, weather);
  }

  @Test
  public void 관측된_기상현상이_없는_경우_날씨군파서는_빈_리스트를_갖는_날씨씨군객체를_반환한다() {
    String rawText = "KSFO 030953Z 29008KT 10SM FEW025 SCT250 18/12 A2995 RMK AO2 SLP142 T01780122=";

    WeatherGroup actual = wxGroupParser.parse(rawText);
    WeatherGroup expected = WeatherGroup.builder()
        .weatherList(List.of())
        .build();

    assertEquals(expected, actual);
  }

  @Test
  public void 단일_기상현상만_관측된_경우_phenomena의_길이가_1인_Weather객체를_반환한다() {
    // given
    String rawText = "METAR RKSI 030300Z 29008KT 260V320 9999 -RA BKN006 OVC045 11/10 Q1009 NOSIG=";

    // when
    Weather weather = wxParser.parse(rawText);

    // then
    Weather expected = Weather.builder()
        .intensity(WeatherInensity.LIGHT)
        .descriptor(List.of())
        .phenomena(List.of(WeatherPhenomenon.RA))
        .build();


    assertAll(
        () -> assertEquals(expected, weather),
        () -> assertEquals(1, weather.getPhenomena().size())
    );
  }

  @Test
  public void 복합_기상현상이_존재하는_경우_phenomena의_길이가_1이상인_Weather객체를_반환한다() {
    // given
    String rawText = "METAR RKTU 030300Z 04002KT 1200 RASN BR BKN010 OVC030 08/08 Q1008 RMK CIG010 SLP085 8/7// 9/8//=";

    // when
    Weather weather = wxParser.parse(rawText);

    // then
    Weather expected = Weather.builder()
        .intensity(WeatherInensity.MODERATE)
        .descriptor(List.of())
        .phenomena(List.of(WeatherPhenomenon.RA, WeatherPhenomenon.SN))
        .build();


    assertAll(
        () -> assertEquals(expected, weather),
        () -> assertEquals(2, weather.getPhenomena().size())
    );
  }

  @Test
  public void 복합_기상현상이_여러개_존재하는_경우_WeatherGroup의_size가_1이상이고_WeatherList의_사이즈도_1이상이다() {
    String rawText ="METAR RKTU 030300Z 04002KT 1200 -TSSNRA -PLSN BR BKN010 OVC030 08/08 Q1008 RMK CIG010 SLP085 8/7// 9/8//=";

    // when
    WeatherGroup weatherGroup = wxGroupParser.parse(rawText);

    // then
    Weather expected1 = Weather.builder()
        .intensity(WeatherInensity.LIGHT)
        .descriptor(List.of(WeatherDescriptor.TS))
        .phenomena(List.of(WeatherPhenomenon.SN, WeatherPhenomenon.RA))
        .build();

    Weather expected2 = Weather.builder()
        .intensity(WeatherInensity.LIGHT)
        .descriptor(List.of())
        .phenomena(List.of(WeatherPhenomenon.PL, WeatherPhenomenon.SN))
        .build();

    Weather expected3 = Weather.builder()
        .intensity(WeatherInensity.MODERATE)
        .descriptor(List.of())
        .phenomena(List.of(WeatherPhenomenon.BR))
        .build();

    WeatherGroup expected = WeatherGroup.builder()
        .weatherList(List.of(expected1, expected2, expected3))
        .build();

    assertEquals(expected, weatherGroup);
  }

}
