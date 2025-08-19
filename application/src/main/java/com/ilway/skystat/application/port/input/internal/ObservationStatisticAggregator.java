package com.ilway.skystat.application.port.input.internal;

import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class ObservationStatisticAggregator {

	public static ObservationStatisticResponse aggregate(List<Metar> metarList, Predicate<Metar> predicate) {
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

		List<MonthlyCountDto> monthly = monthSet.entrySet().stream()
			                                .map(e -> new MonthlyCountDto(e.getKey(), e.getValue().size()))
			                                .sorted(Comparator.comparing(MonthlyCountDto::yearMonth))
			                                .toList();

		List<HourlyCountDto> hourly = hourSet.entrySet().stream()
			                              .flatMap(e -> e.getValue().entrySet().stream()
				                               .map(hr -> new HourlyCountDto(e.getKey(), hr.getKey(), hr.getValue().size()))
			                              )
			                              .sorted(Comparator
				                               .comparing(HourlyCountDto::yearMonth)
				                               .thenComparing(HourlyCountDto::hourUtc)
			                              ).toList();

		return new ObservationStatisticResponse(monthly, hourly);
	}

}
