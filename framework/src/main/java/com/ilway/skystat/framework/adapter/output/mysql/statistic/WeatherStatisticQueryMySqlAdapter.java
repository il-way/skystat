package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.model.weather.WeatherConditionPredicate;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.StatisticDtoMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWeatherQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ilway.skystat.application.model.weather.WeatherConditionPredicate.extract;

@RequiredArgsConstructor
public class WeatherStatisticQueryMySqlAdapter implements WeatherStatisticQueryOutputPort {

	private MetarWeatherQueryRepository weatherQueryRepository;

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(String icao, RetrievalPeriod period, WeatherCondition condition) {
		WeatherConditionPredicate predicate = condition.predicate();
		List<MonthlyCountQueryDto> counts = switch (predicate) {
			case HAS_PHENOMENA -> weatherQueryRepository.countPhenomenaDaysByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherPhenomenon.class),
				condition.target().size()
			);
			case HAS_DESCRIPTORS -> weatherQueryRepository.countDescriptorDaysByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherDescriptor.class),
				condition.target().size()
			);
			case HAS_DESCRIPTORS_AND_PHENOMENA -> {
				List<WeatherPhenomenon> wp = extract(condition.target(), WeatherPhenomenon.class);
				List<WeatherDescriptor> wd = extract(condition.target(), WeatherDescriptor.class);

				yield weatherQueryRepository.countDescriptorAndPhenomenaDaysByMonth(
					icao, period.fromInclusive(), period.toExclusive(),
					wp,
					wd,
					wp.size(),
					wd.size()
				);
			}
		};

		return counts.stream().map(StatisticDtoMapper::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(String icao, RetrievalPeriod period, WeatherCondition condition) {
		WeatherConditionPredicate predicate = condition.predicate();
		List<HourlyCountQueryDto> counts = switch (predicate) {
			case HAS_PHENOMENA -> weatherQueryRepository.countPhenomenaDaysByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherPhenomenon.class),
				condition.target().size()
			);
			case HAS_DESCRIPTORS -> weatherQueryRepository.countDescriptorDaysByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherDescriptor.class),
				condition.target().size()
			);
			case HAS_DESCRIPTORS_AND_PHENOMENA -> {
				List<WeatherPhenomenon> wp = extract(condition.target(), WeatherPhenomenon.class);
				List<WeatherDescriptor> wd = extract(condition.target(), WeatherDescriptor.class);

				yield weatherQueryRepository.countDescriptorAndPhenomenaDaysByMonthHour(
					icao, period.fromInclusive(), period.toExclusive(),
					wp,
					wd,
					wp.size(),
					wd.size()
				);
			}
		};

		return counts.stream().map(StatisticDtoMapper::mapHourly).toList();
	}

}
