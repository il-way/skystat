package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.port.output.MetarBasicStatisticQueryOutputPort;
import com.ilway.skystat.domain.vo.unit.Unit;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarBasicQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

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

	private AverageSummary map(AverageSummaryQueryDto dto) {
		if (dto == null) {
			return new AverageSummary(0, 0, 0, 0, 0);
		}
		return new AverageSummary(
			dto.avgVisibilityM(),
			dto.avgWindSpeedKt(),
			dto.avgWindPeakKt(),
			dto.avgAltimeterHpa(),
			dto.avgCeilingFt()
		);
	}


}
