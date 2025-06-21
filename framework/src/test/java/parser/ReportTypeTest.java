package parser;

import org.junit.jupiter.api.Test;
import parser.metar.entry.ReportTypeRegexParser;
import parser.shared.ReportParser;
import vo.metar.ReportType;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportTypeTest {
  ReportParser<ReportType> parser = new ReportTypeRegexParser();

  @Test
  public void 리포트타입_파싱_성공() {
    String rksi = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";
    String rkjk = "SPECI RKJK 010647Z 27011KT 1 3/4SM -RA BR OVC004 15/14 A2969 RMK AO2A VIS 1 3/4V3 RAB0555E25RAB42E43DZB43E46RAB46 CIG 004V006 CIG 003 RWY18 SLPNO $=";
    String rkjy = "METAR RKJY 010600Z 18017G28KT 150V220 3200 +TSRA BR FEW010CB BKN025 OVC060 16/16 Q1007 NOSIG RMK TS NW MOV NE OCNL LTGIC=";
    String khyi = "KHYI 010056Z AUTO 20004KT 10SM CLR 09/07 A2999 RMK AO2 SLP153 T00940072";

    assertAll(
        () -> assertEquals(parser.parse(rksi), ReportType.METAR),
        () -> assertEquals(parser.parse(rkjk), ReportType.SPECI),
        () -> assertEquals(parser.parse(rkjy), ReportType.METAR),
        () -> assertEquals(parser.parse(khyi), ReportType.AUTO)
    );
  }
}
