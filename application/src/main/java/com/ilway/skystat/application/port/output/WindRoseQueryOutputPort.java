package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.windrose.MonthlyWindRoseRow;

import java.util.List;

public interface WindRoseQueryOutputPort {

	List<MonthlyWindRoseRow> aggregateDefaultByMonth(String icao, RetrievalPeriod period);

	List<MonthlyCountDto> countVariableByMonth(String icao, RetrievalPeriod period);

}
