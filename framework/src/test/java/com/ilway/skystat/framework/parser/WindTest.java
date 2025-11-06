package com.ilway.skystat.framework.parser;

import com.ilway.skystat.domain.vo.unit.SpeedUnit;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.WindDirection;
import com.ilway.skystat.framework.parser.metar.entry.WindRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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
				                                       .speed(8D)
				                                       .gusts(0D)
				                                       .unit(SpeedUnit.KT)
				                                       .build()
			),
			() -> assertEquals(parser.parse(rkjy), Wind.builder()
				                                       .direction(WindDirection.fixed(180))
				                                       .speed(17D)
				                                       .gusts(28D)
				                                       .unit(SpeedUnit.KT)
				                                       .build()
			),
			() -> assertEquals(parser.parse(khyi), Wind.builder()
				                                       .direction(WindDirection.fixed(200))
				                                       .speed(4D)
				                                       .gusts(0D)
				                                       .unit(SpeedUnit.MPS)
				                                       .build()
			)
		);
	}

	@Test
	public void P99KT_파싱에_성공해야한다() {
		String rksi = "RKSI 010300Z 170P99KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";
		String rkjy = "METAR RKJY 010600Z 17050GP99KT 150V220 3200 +TSRA BR FEW010CB BKN025 OVC060 16/16 Q1007 NOSIG RMK TS NW MOV NE OCNL LTGIC=";
		String zbtj = "ZBTJ,2013-03-26 02:30,ZBTJ 260230Z 220P49MPS 180V250 6000 NSC 09/M03 Q1017 NOSIG";
		String zbhh = "ZBHH,2013-03-26 02:30,ZBHH 260230Z 22045GP49MPS 180V250 6000 NSC 09/M03 Q1017 NOSIG";

		assertAll(
			() -> assertEquals(parser.parse(rksi), Wind.builder()
				                                       .direction(WindDirection.fixed(170))
				                                       .speed(100)
				                                       .gusts(0)
				                                       .unit(SpeedUnit.KT)
				                                       .build()
			),
			() -> assertEquals(parser.parse(rkjy), Wind.builder()
				                                       .direction(WindDirection.fixed(170))
				                                       .speed(50)
				                                       .gusts(100)
				                                       .unit(SpeedUnit.KT)
				                                       .build()
			),
			() -> assertEquals(parser.parse(zbtj), Wind.builder()
				                                       .direction(WindDirection.fixed(220))
				                                       .speed(50)
				                                       .gusts(0)
				                                       .unit(SpeedUnit.MPS)
				                                       .build()
			),
			() -> assertEquals(parser.parse(zbhh), Wind.builder()
				                                       .direction(WindDirection.fixed(220))
				                                       .speed(45)
				                                       .gusts(50)
				                                       .unit(SpeedUnit.MPS)
				                                       .build()
			)
		);
	}


}
