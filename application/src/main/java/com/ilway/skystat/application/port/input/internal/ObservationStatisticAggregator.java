package com.ilway.skystat.application.port.input.internal;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;

public final class ObservationStatisticAggregator {

	public static ObservationStatisticResponse aggregate(List<Metar> metarList,
	                                                     Predicate<Metar> predicate,
	                                                     RetrievalPeriod period) {

		Stream<Metar> filtered = metarList.stream().filter(predicate);

		HashMap<YearMonth, Set<LocalDate>> monthSet = new HashMap<>();
		HashMap<YearMonth, Map<Integer, Set<LocalDate>>> hourSet = new HashMap<>();

		filtered.forEach(m -> {
			ZonedDateTime utc = m.getReportTime();
			YearMonth ym = YearMonth.from(utc);
			LocalDate day = utc.toLocalDate();
			int hr = utc.getHour();

			monthSet
				.computeIfAbsent(ym, k -> new HashSet<>())
				.add(day);

			hourSet
				.computeIfAbsent(ym, k -> new HashMap<>())
				.computeIfAbsent(hr, k -> new HashSet<>())
				.add(day);
		});

		List<YearMonth> months = monthsBetween(period.from(), period.to());

		List<MonthlyCountDto> monthly = months.stream()
			                                .map(ym -> new MonthlyCountDto(ym, monthSet.getOrDefault(ym, Collections.emptySet()).size()))
			                                .toList();

		List<HourlyCountDto> hourly = months.stream()
			                              .flatMap(ym -> {
				                              Map<Integer, Set<LocalDate>> byHour = hourSet.getOrDefault(ym, Collections.emptyMap());
				                              return IntStream.range(0,24)
					                                     .mapToObj(h -> new HourlyCountDto(ym, h, byHour.getOrDefault(h, Collections.emptySet()).size()));
			                              })
			                              .toList();


		return new ObservationStatisticResponse(monthly, hourly);
	}

	public static ObservationStatisticResponse aggregate(Map<YearMonth, Long> countMonthly,
	                                                     Map<YearMonth, Map<Integer, Long>> countHourly,
	                                                     RetrievalPeriod period) {

		List<YearMonth> months = monthsBetween(period.from(), period.to());

		List<MonthlyCountDto> monthly = months.stream()
			                             .map(ym -> new MonthlyCountDto(ym, countMonthly.getOrDefault(ym, 0L)))
			                             .toList();

		List<HourlyCountDto> hourly = months.stream()
			                              .flatMap(ym -> {
				                              Map<Integer, Long> byHour = countHourly.getOrDefault(ym, Collections.emptyMap());
																			return IntStream.range(0,24)
																				       .mapToObj(h -> new HourlyCountDto(ym, h, byHour.getOrDefault(h, 0L)));
			                              })
			                              .toList();

		return new ObservationStatisticResponse(monthly, hourly);
	}

	public static ObservationStatisticResponse peelOffZeroCount(ObservationStatisticResponse response) {
		List<MonthlyCountDto> monthly = response.monthly().stream()
			                                .filter(m -> m.count() != 0)
			                                .toList();

		List<HourlyCountDto> hourly = response.hourly().stream()
			                              .filter(h -> h.count() != 0)
			                              .toList();

		return new ObservationStatisticResponse(monthly, hourly);
	}

	private static List<YearMonth> monthsBetween(ZonedDateTime fromInclusive, ZonedDateTime toExclusive) {
		if (!fromInclusive.isBefore(toExclusive)) return List.of();

		List<YearMonth> result = new ArrayList<>();
		YearMonth fromYm = YearMonth.from(fromInclusive);
		YearMonth toYm = YearMonth.from(toExclusive.minusNanos(1L));

		for (YearMonth ym = fromYm; !ym.isAfter(toYm); ym = ym.plusMonths(1)) {
			result.add(ym);
		}

		return result;
	}

}
