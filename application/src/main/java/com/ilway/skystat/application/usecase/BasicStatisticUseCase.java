package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.domain.vo.unit.Unit;

public interface BasicStatisticUseCase {

	double average(String icao, RetrievalPeriod period, MetricField field, Unit unit);

	AverageSummary averageSummary(String icao, RetrievalPeriod period);

}
