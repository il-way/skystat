package parser;

import org.junit.jupiter.api.Test;
import parser.metar.entry.WindRegexParser;
import parser.shared.ReportParser;
import vo.metar.field.Wind;
import vo.metar.field.WindDirection;
import vo.unit.SpeedUnit;

import static org.junit.jupiter.api.Assertions.*;

public class WindTest {

  ReportParser<Wind> parser = new WindRegexParser();

  @Test
  public void 바람이_관측되지_않은_경우_예외가_발생한다() {
    String rawText = "KSFO 030953Z";

    assertThrows(IllegalArgumentException.class, () -> {
      parser.parse(rawText);
    });
  }

  @Test
  public void Variable_바람의_풍향을_조회하면_예외가_발생한다() {
    String rawText = "SPECI RKJK 010647Z VRB11KT 1 3/4SM -RA BR OVC004 15/14 A2969 RMK AO2A VIS 1 3/4V3 RAB0555E25RAB42E43DZB43E46RAB46 CIG 004V006 CIG 003 RWY18 SLPNO $=";

    assertThrows(IllegalStateException.class, () -> {
      parser.parse(rawText).getDirection().getDegreeOrThrow();
    });
  }

  @Test
  public void 바람_파싱에_성공해야한다() {
    String rksi = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";

    String rkjy = "METAR RKJY 010600Z 18017G28KT 150V220 3200 +TSRA BR FEW010CB BKN025 OVC060 16/16 Q1007 NOSIG RMK TS NW MOV NE OCNL LTGIC=";
    String khyi = "KHYI 010056Z AUTO 20004MPS 10SM CLR 09/07 A2999 RMK AO2 SLP153 T00940072";

    assertAll(
        () -> assertEquals(parser.parse(rksi), Wind.builder()
            .direction(WindDirection.fixed(170))
            .speed(8)
            .gusts(0)
            .unit(SpeedUnit.KT)
            .build()
        ),
        () -> assertEquals(parser.parse(rkjy), Wind.builder()
            .direction(WindDirection.fixed(180))
            .speed(17)
            .gusts(28)
            .unit(SpeedUnit.KT)
            .build()
        ),
        () -> assertEquals(parser.parse(khyi), Wind.builder()
            .direction(WindDirection.fixed(200))
            .speed(4)
            .gusts(0)
            .unit(SpeedUnit.MPS)
            .build()
        )
    );
  }

}
