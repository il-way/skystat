package com.ilway.skystat.application.port.input.internal;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.*;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.stream.Collectors.groupingBy;

public final class TemperatureStatisticAggregator {

	private TemperatureStatisticAggregator() {
	}

	record YearMonthHour(YearMonth ym, int hour) {
	}

	public static TemperatureStatisticResult aggregate(List<Metar> metars, RetrievalPeriod period) {
		List<MonthlyTemperatureStatDto> monthly = monthly(metars, period);
		List<HourlyTemperatureStatDto> hourly = hourly(metars, period);
		List<YearlyTemperatureStatDto> yearly = yearly(metars, period);

		return new TemperatureStatisticResult(monthly, hourly, yearly);
	}

	public static List<MonthlyTemperatureStatDto> monthly(List<DailyTemperatureStatDto> dailies) {
		Map<YearMonth, List<DailyTemperatureStatDto>> byYearMonth = dailies.stream().collect(
			groupingBy(d -> YearMonth.of(d.year(), d.month()))
		);

		return byYearMonth.entrySet().stream()
			       .sorted(Map.Entry.comparingByKey())
			       .map(entry -> {
				       YearMonth ym = entry.getKey();
				       List<DailyTemperatureStatDto> daily = entry.getValue();

				       List<Double> dailyMeans = daily.stream().map(DailyTemperatureStatDto::dailyMean).toList();
				       List<Double> dailyMaxes = daily.stream().map(DailyTemperatureStatDto::dailyMax).toList();
					     List<Double> dailyMins = daily.stream().map(DailyTemperatureStatDto::dailyMin).toList();

				       Double dailyMeanAvg = avgOrNull(dailyMeans);
				       Double dailyMaxAvg = avgOrNull(dailyMaxes);
				       Double dailyMinAvg = avgOrNull(dailyMins);

				       Double monthlyMax = dailyMaxes.stream()
					                           .filter(Objects::nonNull)
					                           .filter(Double::isFinite)
					                           .max(Comparator.naturalOrder())
					                           .orElse(null);

				       Double monthlyMin = dailyMins.stream()
					                           .filter(Objects::nonNull)
					                           .filter(Double::isFinite)
					                           .min(Comparator.naturalOrder())
					                           .orElse(null);

				       return new MonthlyTemperatureStatDto(
					       ym.getYear(),
					       ym.getMonthValue(),
					       dailyMeanAvg, dailyMaxAvg, dailyMinAvg,
					       monthlyMax, monthlyMin
				       );
			       })
			       .toList();
	}

	public static List<MonthlyTemperatureStatDto> monthly(List<Metar> metars, RetrievalPeriod period) {
		Map<YearMonth, List<Metar>> byYearMonth = metars.stream().collect(
			groupingBy(TemperatureStatisticAggregator::getYearMonthFrom)
		);

		return byYearMonth.entrySet().stream()
			       .sorted(Map.Entry.comparingByKey())
			       .map(entry -> {
				       YearMonth ym = entry.getKey();
				       List<Metar> ms = entry.getValue();

				       Map<LocalDate, List<Metar>> byDate = ms.stream().collect(
					       groupingBy(TemperatureStatisticAggregator::getDateFrom)
				       );

				       List<Double> dailyMeans = new ArrayList<>();
				       List<Double> dailyMaxes = new ArrayList<>();
				       List<Double> dailyMins = new ArrayList<>();

				       for (List<Metar> dailyMetars : byDate.values()) {
					       List<Double> dailyTemps = extractCelsiusTemps(dailyMetars);
					       if (dailyTemps.isEmpty()) continue;
					       dailyMeans.add(avgOrNan(dailyTemps));
					       dailyMaxes.add(max(dailyTemps));
					       dailyMins.add(Collections.min(dailyTemps));
				       }

				       Double dailyMeanAvg = avgOrNull(dailyMeans);
				       Double dailyMaxAvg = avgOrNull(dailyMaxes);
				       Double dailyMinAvg = avgOrNull(dailyMins);

				       List<Double> monthlyTemps = extractCelsiusTemps(ms);
				       Double monthlyMax = monthlyTemps.isEmpty() ? null : max(monthlyTemps);
				       Double monthlyMin = monthlyTemps.isEmpty() ? null : Collections.min(monthlyTemps);

				       return new MonthlyTemperatureStatDto(
					       ym.getYear(),
					       ym.getMonthValue(),
					       dailyMeanAvg, dailyMaxAvg, dailyMinAvg,
					       monthlyMax, monthlyMin
				       );
			       })
			       .toList();
	}

	public static List<HourlyTemperatureStatDto> hourly(List<Metar> metars, RetrievalPeriod period) {
		Map<YearMonthHour, List<Metar>> byYearMonthHour = metars.stream()
			                                                  .collect(groupingBy(m ->
				                                                                      new YearMonthHour(getYearMonthFrom(m), getHourFrom(m))
				                                                  )
			                                                  );

		return byYearMonthHour.entrySet().stream()
			       .sorted(Comparator
				               .comparing((Map.Entry<YearMonthHour, List<Metar>> entry) -> entry.getKey().ym())
				               .thenComparing(e -> e.getKey().hour()))
			       .map(e -> {
				       YearMonthHour ymh = e.getKey();
				       List<Double> temps = extractCelsiusTemps(e.getValue());

				       Double mean = avgOrNull(temps);
				       Double max = temps.isEmpty() ? null : max(temps);
				       Double min = temps.isEmpty() ? null : Collections.min(temps);
				       return new HourlyTemperatureStatDto(
					       ymh.ym().getYear(),
					       ymh.ym().getMonthValue(),
					       ymh.hour(),
					       mean, max, min
				       );
			       })
			       .toList();
	}

	public static List<YearlyTemperatureStatDto> yearly(List<DailyTemperatureStatDto> dailies) {
		Map<Integer, List<DailyTemperatureStatDto>> byYear = dailies.stream().collect(
			groupingBy(DailyTemperatureStatDto::year)
		);

		return byYear.entrySet().stream()
			       .sorted(Map.Entry.comparingByKey())
			       .map(e -> {
				       int year = e.getKey();
				       List<DailyTemperatureStatDto> yearly = e.getValue();

				       List<Double> dailyMeans = yearly.stream().map(DailyTemperatureStatDto::dailyMean).toList();
				       List<Double> dailyMaxes = yearly.stream().map(DailyTemperatureStatDto::dailyMax).toList();
				       List<Double> dailyMins = yearly.stream().map(DailyTemperatureStatDto::dailyMin).toList();

				       Double dailyMeanAvg = avgOrNull(dailyMeans);
				       Double dailyMaxAvg = avgOrNull(dailyMaxes);
				       Double dailyMinAvg = avgOrNull(dailyMins);

				       Double yearlyMax = dailyMaxes.stream()
					                           .filter(Objects::nonNull)
					                           .filter(Double::isFinite)
					                           .max(Comparator.naturalOrder())
					                           .orElse(null);

				       Double yearlyMin = dailyMins.stream()
					                           .filter(Objects::nonNull)
					                           .filter(Double::isFinite)
					                           .min(Comparator.naturalOrder())
					                           .orElse(null);

				       return new YearlyTemperatureStatDto(
					       year,
					       dailyMeanAvg, dailyMaxAvg, dailyMinAvg,
					       yearlyMax, yearlyMin
				       );
			       })
			       .toList();
	}

	public static List<YearlyTemperatureStatDto> yearly(List<Metar> metars, RetrievalPeriod period) {
		Map<Integer, List<Metar>> byYear = metars.stream()
			                                   .collect(groupingBy(m -> m.getReportTime().getYear()));

		return byYear.entrySet().stream()
			       .sorted(Map.Entry.comparingByKey())
			       .map(e -> {
				       int year = e.getKey();
				       List<Metar> ms = e.getValue();

				       Map<LocalDate, List<Metar>> byDate = ms.stream().collect(groupingBy(TemperatureStatisticAggregator::getDateFrom));

				       List<Double> dailyMeans = new ArrayList<>();
				       List<Double> dailyMaxes = new ArrayList<>();
				       List<Double> dailyMins = new ArrayList<>();

				       for (List<Metar> dailyMetar : byDate.values()) {
					       List<Double> temps = extractCelsiusTemps(dailyMetar);
					       if (temps.isEmpty()) continue;
					       dailyMeans.add(avgOrNan(temps));
					       dailyMaxes.add(max(temps));
					       dailyMins.add(min(temps));
				       }

				       Double dailyMeanAvg = avgOrNull(dailyMeans);
				       Double dailyMaxAvg = avgOrNull(dailyMaxes);
				       Double dailyMinAvg = avgOrNull(dailyMins);

				       List<Double> yearlyTemps = extractCelsiusTemps(ms);
				       Double yearlyMax = yearlyTemps.isEmpty() ? null : max(yearlyTemps);
				       Double yearlyMin = yearlyTemps.isEmpty() ? null : min(yearlyTemps);

				       return new YearlyTemperatureStatDto(
					       year, dailyMeanAvg, dailyMaxAvg, dailyMinAvg, yearlyMax, yearlyMin
				       );
			       })
			       .toList();
	}

	private static List<Double> extractCelsiusTemps(List<Metar> metars) {
		return metars.stream()
			       .map(m -> m.getTemperature() != null
				                 ? m.getTemperature().getUnit().convertTo(m.getTemperature().getValue(), CELSIUS)
				                 : null)
			       .filter(Objects::nonNull)
			       .toList();
	}

	private static double avgOrNan(List<Double> values) {
		return values.stream()
			       .filter(Objects::nonNull)
			       .mapToDouble(Double::doubleValue)
			       .filter(Double::isFinite)
			       .average()
			       .orElse(Double.NaN);
	}

	private static Double avgOrNull(List<Double> values) {
		if (values == null || values.isEmpty()) return null;
		DoubleSummaryStatistics summary = values.stream()
			                                  .filter(Objects::nonNull)
			                                  .mapToDouble(Double::doubleValue)
			                                  .filter(d -> !Double.isNaN(d) && !Double.isInfinite(d))
			                                  .summaryStatistics();

		return summary.getCount() == 0 ? null : summary.getAverage();
	}

	private static YearMonth getYearMonthFrom(Metar m) {
		return YearMonth.from(m.getReportTime());
	}

	private static LocalDate getDateFrom(Metar m) {
		return m.getReportTime().toLocalDate(); // UTC기준 Date
	}

	private static int getHourFrom(Metar m) {
		return m.getReportTime().getHour(); // UTC기준의 0~23시
	}

}
