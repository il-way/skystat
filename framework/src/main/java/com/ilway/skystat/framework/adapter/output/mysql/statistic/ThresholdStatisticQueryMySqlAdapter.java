package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.model.generic.Comparison;
import com.ilway.skystat.application.model.weather.*;
import com.ilway.skystat.application.port.output.ThresholdStatisticQueryOutputPort;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.StatisticDtoMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarMetricQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ilway.skystat.application.model.generic.Comparison.GTE;
import static com.ilway.skystat.application.model.generic.Comparison.LTE;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.PressureUnit.HPA;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.framework.adapter.output.mysql.mapper.StatisticDtoMapper.when;
import static java.math.RoundingMode.HALF_UP;

@TranslateDbExceptions("querying threshold statistic")
@RequiredArgsConstructor
public class ThresholdStatisticQueryMySqlAdapter implements ThresholdStatisticQueryOutputPort {

	private final MetarMetricQueryRepository metricQueryRepository;

	@Override
	public List<MonthlyCountDto> countDistinctDaysByMonth(@UppercaseParam String icao, RetrievalPeriod period, ThresholdCondition condition) {
		MetricField field = condition.field();
		Comparison comparison = condition.comparison();
		List<MonthlyCountQueryDto> counts = switch (field) {
			case VISIBILITY -> when(comparison.equals(LTE), () -> metricQueryRepository.countVisibilityMeterLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), METERS, RoundingPolicy.of(0, HALF_UP))
			));
			case WIND_SPEED -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindSpeedKtGteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT, RoundingPolicy.of(0, HALF_UP))
			));
			case WIND_PEAK -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindPeakKtGteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT, RoundingPolicy.of(0, HALF_UP))
			));
			case CEILING -> when(comparison.equals(LTE), () -> metricQueryRepository.countCeilingLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), FEET)
			));
			case ALTIMETER -> when(comparison.equals(LTE), () -> metricQueryRepository.countAltimeterHpaLteByMonth(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), HPA, RoundingPolicy.of(2, HALF_UP))
			));
		};

		return counts.stream().map(StatisticDtoMapper::mapMonthly).toList();
	}

	@Override
	public List<HourlyCountDto> countDistinctHoursByMonth(@UppercaseParam String icao, RetrievalPeriod period, ThresholdCondition condition) {
		MetricField field = condition.field();
		Comparison comparison = condition.comparison();
		List<HourlyCountQueryDto> counts = switch (field) {
			case VISIBILITY -> when(comparison.equals(LTE), () -> metricQueryRepository.countVisibilityMeterLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), METERS, RoundingPolicy.of(0, HALF_UP))
			));
			case WIND_SPEED -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindSpeedKtGteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT, RoundingPolicy.of(0, HALF_UP))
			));
			case WIND_PEAK -> when(comparison.equals(GTE), () -> metricQueryRepository.countWindPeakKtGteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), KT, RoundingPolicy.of(0, HALF_UP))
			));
			case CEILING -> when(comparison.equals(LTE), () -> metricQueryRepository.countCeilingLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), FEET)
			));
			case ALTIMETER -> when(comparison.equals(LTE), () -> metricQueryRepository.countAltimeterHpaLteByMonthHour(
				icao,
				period.fromInclusive(),
				period.toExclusive(),
				condition.unit().convertTo(condition.threshold(), HPA, RoundingPolicy.of(2, HALF_UP))
			));
		};

		return counts.stream().map(StatisticDtoMapper::mapHourly).toList();
	}


}
