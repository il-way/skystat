package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.port.output.MetarStatisticSummaryQueryOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarSummaryQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountSummaryQueryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MetarStatisticSummaryQueryMySqlAdapter implements MetarStatisticSummaryQueryOutputPort {

	private final MetarSummaryQueryRepository repository;

	@Override
	public AverageSummary averageSummary(String icao, RetrievalPeriod period) {
		return AverageSummaryQueryDto.map(repository.averageSummary(icao, period.fromInclusive(), period.toExclusive()));
	}

	@Override
	public List<MonthlyCountSummaryDto> countSummaryByMonth(String icao, RetrievalPeriod period, MonthlyCountSummaryTarget target) {
		List<MonthlyCountSummaryQueryDto> result = repository.countSummaryByMonth(
			icao,
			period.fromInclusive(),
			period.toExclusive(),
			target.windPeakThreshold(),
			target.visibilityThreshold(),
			target.ceilingThreshold(),
			target.phenomenon(),
			target.descriptor()
		);

		return result.stream()
			       .map(MonthlyCountSummaryQueryDto::map)
			       .toList();
	}
}
