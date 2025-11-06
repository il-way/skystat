package com.ilway.skystat.application.port.output;

import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.YearMonth;

public interface MetarParsingOutputPort {

	Metar parse(String rawText, YearMonth observationYearMonth);

}
