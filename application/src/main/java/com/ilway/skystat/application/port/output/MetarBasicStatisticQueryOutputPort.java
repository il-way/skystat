package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.domain.vo.unit.Unit;

public interface MetarBasicStatisticQueryOutputPort {

	double average(String icao, RetrievalPeriod period, MetricField field, Unit unit);

	AverageSummary averageSummary(String icao, RetrievalPeriod period);

}
