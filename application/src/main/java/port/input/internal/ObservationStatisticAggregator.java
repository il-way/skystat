//package port.input.internal;
//
//import dto.statistic.ObservationStatisticResponse;
//import vo.metar.Metar;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.*;
//import java.util.function.Predicate;
//import java.util.stream.Stream;
//
//public final class ObservationStatisticAggregator {
//
//	public static ObservationStatisticResponse aggregate(List<Metar> metarList, Predicate<Metar> predicate) {
//		Stream<Metar> filtered = metarList.stream().filter(predicate);
//
//		HashMap<YearMonth, Set<LocalDate>> monthSet = new HashMap<>();
//		HashMap<YearMonth, Map<Integer, Set<LocalDate>>> hourSet = new HashMap<>();
//
//		filtered.forEach(m -> {
//			ZonedDateTime utc = m.getReportTime();
//			YearMonth ym = YearMonth.from(utc);
//			LocalDate day = utc.toLocalDate();
//			int hr = utc.getHour();
//
//			monthSet
//				.computeIfAbsent(ym, k -> new HashSet<>())
//				.add(day);
//
//			hourSet
//				.computeIfAbsent(ym, k -> new HashMap<>())
//				.computeIfAbsent(hr, k -> new HashSet<>())
//				.add(day);
//		});
//
//	}
//
//}
