package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.domain.vo.unit.Unit;

import java.util.List;

public interface BasicStatisticUseCase {

	double average(String icao, RetrievalPeriod period, MetricField field, Unit unit);

	List<MonthlyAverageDto> averageMonthly(String icao, RetrievalPeriod period, MetricField field, Unit unit);

}
