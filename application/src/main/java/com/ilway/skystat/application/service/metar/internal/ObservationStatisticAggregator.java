package com.ilway.skystat.application.service.metar.internal;

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
	                                                   List<HourlyCountDto> hourly,
	                                                   RetrievalPeriod period) {

		List<MonthlyCountDto> normalizeMonthly = normalizeMonthly(monthly, period);
		List<HourlyCountDto> normalizeHourly = normalizeHourly(hourly, period);
		return new ObservationStatisticResult(normalizeMonthly, normalizeHourly);
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

	private static List<MonthlyCountDto> normalizeMonthly(List<MonthlyCountDto> monthly, RetrievalPeriod period) {
		// 같은 (연/월) 항목 중복이 있을 수 있으니 합산 집계
		Map<YearMonth, Long> byMonth = new HashMap<>();
		for (MonthlyCountDto m : monthly) {
			YearMonth ym = YearMonth.of(m.year(), m.month());
			byMonth.merge(ym, m.count(), Long::sum);
		}

		List<YearMonth> months = monthsBetween(period.fromInclusive(), period.toExclusive());
		return months.stream()
			       .map(ym -> new MonthlyCountDto(ym.getYear(), ym.getMonthValue(), byMonth.getOrDefault(ym, 0L)))
			       .toList();
	}

	private static List<HourlyCountDto> normalizeHourly(List<HourlyCountDto> hourly,
	                                                    RetrievalPeriod period) {
		record Key(YearMonth ym, int hour) {}
		Map<Key, Long> byKey = new HashMap<>();
		if (hourly != null) {
			for (HourlyCountDto h : hourly) {
				Key k = new Key(YearMonth.of(h.year(), h.month()), h.hour());
				byKey.merge(k, h.count(), Long::sum);
			}
		}

		List<YearMonth> months = monthsBetween(period.fromInclusive(), period.toExclusive());
		List<HourlyCountDto> out = new ArrayList<>(months.size() * 24);
		for (YearMonth ym : months) {
			for (int h = 0; h < 24; h++) {
				long c = byKey.getOrDefault(new Key(ym, h), 0L);
				out.add(new HourlyCountDto(ym.getYear(), ym.getMonthValue(), h, c));
			}
		}
		return out;
	}


}
