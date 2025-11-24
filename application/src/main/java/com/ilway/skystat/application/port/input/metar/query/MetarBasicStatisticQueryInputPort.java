package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.port.output.MetarBasicStatisticQueryOutputPort;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarBasicStatisticQueryInputPort implements BasicStatisticUseCase {

	private final MetarBasicStatisticQueryOutputPort outputPort;

	@Override
	public double average(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		return outputPort.average(icao, period, field, unit);
	}

	@Override
	public List<MonthlyAverageDto> averageMonthly(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		return outputPort.averageMonthly(icao, period, field, unit);
	}

}
