package com.ilway.skystat.framework.adapter.output;

import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.parser.metar.MetarParser;

import java.time.YearMonth;

public class MetarParsingAdapter implements MetarParsingOutputPort {

	@Override
	public Metar parse(String rawText, YearMonth observationYearMonth) {
		MetarParser parser = new MetarParser(observationYearMonth);
		return parser.parse(rawText);
	}

}
