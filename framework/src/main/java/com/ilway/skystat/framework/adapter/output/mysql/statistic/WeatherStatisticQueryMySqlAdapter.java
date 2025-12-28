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
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ilway.skystat.application.model.weather.WeatherConditionPredicate.extract;
import static java.util.stream.Collectors.*;

@TranslateDbExceptions("querying weather statistic")
@RequiredArgsConstructor
public class WeatherStatisticQueryMySqlAdapter implements WeatherStatisticQueryOutputPort {

	private final MetarWeatherQueryRepository weatherQueryRepository;

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(@UppercaseParam String icao, RetrievalPeriod period, WeatherCondition condition) {
		WeatherConditionPredicate predicate = condition.predicate();
		List<MonthlyCountQueryDto> counts = switch (predicate) {
			case HAS_PHENOMENA -> weatherQueryRepository.countPhenomenaExistAnyDaysByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherPhenomenon.class),
				condition.target().size(),
				extract(condition.target(), WeatherPhenomenon.class).stream()
					.map(Enum::name)
					.collect(joining())
			);
			case HAS_DESCRIPTORS -> weatherQueryRepository.countDescriptorExistAnyDaysByMonthNative(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherDescriptor.class),
				condition.target().size(),
				extract(condition.target(), WeatherDescriptor.class).stream()
					.map(Enum::name)
					.collect(joining())
			).stream().map(MonthlyCountQueryDto::from).toList();
			case HAS_DESCRIPTORS_AND_PHENOMENA -> {
				List<WeatherDescriptor> wd = extract(condition.target(), WeatherDescriptor.class);
				List<WeatherPhenomenon> wp = extract(condition.target(), WeatherPhenomenon.class);
				String wdCode = wd.stream().map(Enum::name).collect(joining());
				String wpCode = wp.stream().map(Enum::name).collect(joining());

				yield weatherQueryRepository.countDescriptorAndPhenomenaExsitAnyDaysByMonthNative(
					icao, period.fromInclusive(), period.toExclusive(),
					wd,
					wp,
					wd.size(),
					wp.size(),
					(wdCode + wpCode)
				).stream().map(MonthlyCountQueryDto::from).toList();
			}
		};

		return counts.stream().map(StatisticDtoMapper::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(@UppercaseParam String icao, RetrievalPeriod period, WeatherCondition condition) {
		WeatherConditionPredicate predicate = condition.predicate();
		List<HourlyCountQueryDto> counts = switch (predicate) {
			case HAS_PHENOMENA -> weatherQueryRepository.countPhenomenaExistAnyDaysByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherPhenomenon.class),
				condition.target().size(),
				extract(condition.target(), WeatherPhenomenon.class).stream()
					.map(Enum::name)
					.collect(joining())
			);
			case HAS_DESCRIPTORS -> weatherQueryRepository.countDescriptorExistAnyDaysByMonthHourNative(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				extract(condition.target(), WeatherDescriptor.class),
				condition.target().size(),
				extract(condition.target(), WeatherDescriptor.class).stream()
					.map(Enum::name)
					.collect(joining())
			).stream().map(HourlyCountQueryDto::from).toList();
			case HAS_DESCRIPTORS_AND_PHENOMENA -> {
				List<WeatherDescriptor> wd = extract(condition.target(), WeatherDescriptor.class);
				List<WeatherPhenomenon> wp = extract(condition.target(), WeatherPhenomenon.class);
				String wdCode = wd.stream().map(Enum::name).collect(joining());
				String wpCode = wp.stream().map(Enum::name).collect(joining());

				yield weatherQueryRepository.countDescriptorAndPhenomenaExistAnyDaysByMonthHourNative(
					icao, period.fromInclusive(), period.toExclusive(),
					wd,
					wp,
					wd.size(),
					wp.size(),
					(wdCode + wpCode)
				).stream().map(HourlyCountQueryDto::from).toList();
			}
		};

		return counts.stream().map(StatisticDtoMapper::mapHourly).toList();
	}

}
