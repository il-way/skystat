package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.model.generic.Comparison;
import com.ilway.skystat.application.model.weather.*;
import com.ilway.skystat.application.port.output.CloudStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.application.port.output.WeatherStatisticQueryOutputPort;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarCloudQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarMetricQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWeatherQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

import static com.ilway.skystat.application.model.generic.Comparison.GTE;
import static com.ilway.skystat.application.model.generic.Comparison.LTE;
import static com.ilway.skystat.application.model.weather.WeatherConditionPredicate.extract;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.HPA;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;

@RequiredArgsConstructor
public class MetarStatisticQueryMySqlAdapter implements WeatherStatisticQueryOutputPort, CloudStatisticQueryOutputPort, ThresholdStatisticQueryOutputPort {

	private final MetarMetricQueryRepository metricQueryRepository;
	private final MetarWeatherQueryRepository weatherQueryRepository;
	private final MetarCloudQueryRepository cloudQueryRepository;

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(String icao, RetrievalPeriod period, CloudCondition condition) {
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

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(String icao, RetrievalPeriod period, CloudCondition condition) {
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

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapHourly).toList();
	}

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition) {
		MetricField field = condition.field();
		Comparison comparison = condition.comparison();
		List<MonthlyCountQueryDto> counts = switch (field) {
			case VISIBILITY -> when(comparison.equals(LTE), () -> metricQueryRepository.countVisibilityMeterLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), METERS)
			));
			case WIND_SPEED -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindSpeedKtGteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT)
			));
			case PEAK_WIND -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindPeakKtGteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT)
			));
			case LOWEST_CEILING -> when(comparison.equals(LTE), () -> metricQueryRepository.countCeilingLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), FEET)
			));
			case ALTIMETER -> when(comparison.equals(LTE), () -> metricQueryRepository.countAltimeterHpaLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), HPA)
			));
		};

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(String icao, RetrievalPeriod period, ThresholdCondition condition) {
		MetricField field = condition.field();
		Comparison comparison = condition.comparison();
		List<HourlyCountQueryDto> counts = switch (field) {
			case VISIBILITY -> when(comparison.equals(LTE), () -> metricQueryRepository.countVisibilityMeterLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), METERS)
			));
			case WIND_SPEED -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindSpeedKtGteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT)
			));
			case PEAK_WIND -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindPeakKtGteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT)
			));
			case LOWEST_CEILING -> when(comparison.equals(LTE), () -> metricQueryRepository.countCeilingLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), FEET)
			));
			case ALTIMETER -> when(comparison.equals(LTE), () -> metricQueryRepository.countAltimeterHpaLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), HPA)
			));
		};

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapHourly).toList();
	}

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

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapMonthly).toList();
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

		return counts.stream().map(MetarStatisticQueryMySqlAdapter::mapHourly).toList();
	}

	private static MonthlyCountDto mapMonthly(MonthlyCountQueryDto c) {
		return new MonthlyCountDto(c.year(), c.month(), c.count());
	}

	private static HourlyCountDto mapHourly(HourlyCountQueryDto c) {
		return new HourlyCountDto(c.year(), c.month(), c.hour(), c.count());
	}

	private static <T> List<T> when(boolean isSatisfiedBy, Supplier<List<T>> body) {
		if (!isSatisfiedBy) throw new AggregationUnavailableException("Query is not supplied.");
		return body.get();
	}

}
