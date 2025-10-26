package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.port.output.MetarBasicStatisticQueryOutputPort;
import com.ilway.skystat.domain.vo.unit.Unit;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarBasicQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.ilway.skystat.application.model.weather.MetricField.*;

@RequiredArgsConstructor
public class MetarBasicStatisticQueryMySqlAdapter implements MetarBasicStatisticQueryOutputPort {

	private final MetarBasicQueryRepository repository;

	@Override
	public double average(@UppercaseParam String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		throw new BusinessException(501, "NOT_IMPLEMENTED", "Average each field is not implemented yet.");
	}

	@Override
	public AverageSummary averageSummary(@UppercaseParam String icao, RetrievalPeriod period) {
		AverageSummaryQueryDto result = repository.averageSummary(icao, period.fromInclusive(), period.toExclusive());
		return map(result);
	}

	@Override
	public List<MonthlyAverageDto> averageMonthly(String icao, RetrievalPeriod period, MetricField field, Unit unit) {
		return switch (field) {
			case WIND_SPEED ->
				repository.averageWindSpeedKtMonthly(icao, period.fromInclusive(), period.toExclusive())
					.stream()
					.map(dto -> new MonthlyAverageDto(dto.month(), dto.value()))
					.toList();
			default -> List.of();
		};
	}

	private AverageSummary map(AverageSummaryQueryDto dto) {
		if (dto == null) {
			return new AverageSummary(0, 0, 0, 0, 0);
		}
		return new AverageSummary(
			nvl(dto.avgVisibilityM()),
			nvl(dto.avgWindSpeedKt()),
			nvl(dto.avgWindPeakKt()),
			nvl(dto.avgAltimeterHpa()),
			nvl(dto.avgCeilingFt())
		);
	}

	private static double nvl(Double v) {
		return v != null ? v : 0d;
	}


}
