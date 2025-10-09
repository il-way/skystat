package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.model.weather.CloudConditionPredicate;
import com.ilway.skystat.application.port.output.CloudStatisticQueryOutputPort;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.StatisticDtoMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarCloudQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

@TranslateDbExceptions("querying cloud statistic")
@RequiredArgsConstructor
public class CloudStatisticQueryMySqlAdapter implements CloudStatisticQueryOutputPort {

	private final MetarCloudQueryRepository cloudQueryRepository;

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(@UppercaseParam String icao, RetrievalPeriod period, CloudCondition condition) {
		CloudConditionPredicate predicate = condition.predicate();
		List<MonthlyCountQueryDto> counts = switch (predicate) {
			case HAS_CLOUDTYPE -> cloudQueryRepository.countCloudTypeDaysByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				(CloudType) condition.target()
			);
			case HAS_COVERAGE -> cloudQueryRepository.countCloudCoverageDaysByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				(CloudCoverage) condition.target()
			);
		};

		return counts.stream().map(StatisticDtoMapper::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(@UppercaseParam String icao, RetrievalPeriod period, CloudCondition condition) {
		CloudConditionPredicate predicate = condition.predicate();
		List<HourlyCountQueryDto> counts = switch (predicate) {
			case HAS_CLOUDTYPE -> cloudQueryRepository.countCloudTypeDaysByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				(CloudType) condition.target()
			);
			case HAS_COVERAGE -> cloudQueryRepository.countCloudCoverageDaysByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				(CloudCoverage) condition.target()
			);
		};

		return counts.stream().map(StatisticDtoMapper::mapHourly).toList();
	}

}
