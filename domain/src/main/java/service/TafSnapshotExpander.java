package service;

import vo.taf.Taf;
import vo.taf.field.ForecastPeriod;
import vo.taf.field.TafSection;
import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;

import java.time.ZonedDateTime;
import java.util.*;

public final class TafSnapshotExpander {

	public List<WeatherSnapshot> expand(Taf taf) {
		Objects.requireNonNull(taf, "taf must not be null");

		NavigableMap<ZonedDateTime, WeatherSnapshot> slots = initSlots(taf.getValidPeriod());
		for (TafSection section : taf.getSections()) {
			for (ZonedDateTime t: hours(section.getPeriod(), section.getModifier(), taf)) {
				WeatherSnapshot base = slots.get(t);
				WeatherSnapshot patched = base.overrideWith(section.getWeather(), section.getModifier());
				slots.put(t, patched);
			}
		}
		return new ArrayList<>(slots.values());
	}

	private NavigableMap<ZonedDateTime, WeatherSnapshot> initSlots(ForecastPeriod period) {
		NavigableMap<ZonedDateTime, WeatherSnapshot> map = new TreeMap<>();

		ZonedDateTime from = period.getFrom();
		ZonedDateTime to = period.getTo();
		for (ZonedDateTime t=from; t.isBefore(to); t=t.plusHours(1)) {
			map.put(t, WeatherSnapshot.empty(t));
		}

		return map;
	}

	private static Iterable<ZonedDateTime> hours(ForecastPeriod period, Modifier modifier, Taf taf) {
		List<ZonedDateTime> list = new ArrayList<>();

		ZonedDateTime from = period.getFrom();
		ZonedDateTime to = period.getTo();
		if (modifier == Modifier.BECMG) {
			from = to;
			to = taf.getValidPeriod().getTo();
		}

		if (modifier == Modifier.FM) {
			to = taf.getValidPeriod().getTo();
		}

		for (ZonedDateTime t=from; t.isBefore(to); t=t.plusHours(1)) {
			list.add(t);
		}

		return list;
	}

}
