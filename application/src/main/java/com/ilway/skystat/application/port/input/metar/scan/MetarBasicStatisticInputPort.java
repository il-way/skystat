package com.ilway.skystat.application.port.input.metar.scan;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.unit.Unit;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarBasicStatisticInputPort implements BasicStatisticUseCase {

	private final MetarManagementOutputPort outputPort;

	@Override
	public double average(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
//		List<Metar> metars = outputPort.findByIcaoAndReportTimePeriod(icao, period);
		return 0;
	}

	@Override
	public List<MonthlyAverageDto> averageMonthly(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		return List.of();
	}

}
