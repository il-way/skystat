package com.ilway.skystat.application.port.input.internal;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ObservationStatisticAggregator {

	private ObservationStatisticAggregator() {}

	public static ObservationStatisticResult aggregate(List<Metar> metars,
	                                                   Predicate<Metar> predicate,
	                                                   RetrievalPeriod period) {

		Stream<Metar> filtered = metars.stream().filter(predicate);

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

		List<YearMonth> months = monthsBetween(period.fromInclusive(), period.toExclusive());

		List<MonthlyCountDto> monthly = months.stream()
			                                .map(ym -> new MonthlyCountDto(
				                                ym.getYear(),
				                                ym.getMonthValue(),
				                                monthSet.getOrDefault(ym, Collections.emptySet()).size())
			                                )
			                                .toList();

		List<HourlyCountDto> hourly = months.stream()
			                              .flatMap(ym -> {
				                              Map<Integer, Set<LocalDate>> byHour = hourSet.getOrDefault(ym, Collections.emptyMap());
				                              return IntStream.range(0, 24)
					                                     .mapToObj(h -> new HourlyCountDto(
							                                     ym.getYear(),
							                                     ym.getMonthValue(),
							                                     h,
							                                     byHour.getOrDefault(h, Collections.emptySet()).size()
						                                     )
					                                     );
			                              })
			                              .toList();


		return new ObservationStatisticResult(monthly, hourly);
	}

	public static ObservationStatisticResult aggregate(List<MonthlyCountDto> monthly,
	                                                   List<HourlyCountDto> hourly) {

		return new ObservationStatisticResult(monthly, hourly);
	}

	public static ObservationStatisticResult peelOffZeroCount(ObservationStatisticResult response) {
		List<MonthlyCountDto> monthly = response.monthly().stream()
			                                .filter(m -> m.count() != 0)
			                                .toList();

		List<HourlyCountDto> hourly = response.hourly().stream()
			                              .filter(h -> h.count() != 0)
			                              .toList();

		return new ObservationStatisticResult(monthly, hourly);
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
