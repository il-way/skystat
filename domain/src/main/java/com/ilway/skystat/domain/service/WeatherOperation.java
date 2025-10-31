package com.ilway.skystat.domain.service;

import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WeatherOperation {

	public static boolean containsPhenomena(Weathers wg, List<WeatherPhenomenon> target) {
		return wg.getWeathers().stream()
			       .anyMatch(w -> containsPhenomena(w, target, false));
	}

	public static boolean containsDescriptors(Weathers wg, List<WeatherDescriptor> target) {
		return wg.getWeathers().stream()
			.anyMatch(w -> containsDescriptors(w, target, false));
	}

	public static boolean containsDescriptorsAndPhenomena(Weathers wg, List<WeatherDescription> target) {
		if (target == null) {
			throw new IllegalArgumentException("target must not be null");
		}
		if (target.isEmpty()) {
			return true;
		}

		List<WeatherDescriptor> targetDescriptors = new ArrayList<>();
		List<WeatherPhenomenon> targetPhenomena = new ArrayList<>();

		boolean seenPhenomenon = false;
		for (WeatherDescription d : target) {
			if (d instanceof WeatherDescriptor wd) {
				if (seenPhenomenon) return false; // 현상(Phenomena) 뒤에 기술(Descriptor)이 나오면 형식 위반
				targetDescriptors.add(wd);
			} else if (d instanceof WeatherPhenomenon wp) {
				seenPhenomenon = true;
				targetPhenomena.add(wp);
			} else {
				return false; // 이 타입 외에는 허용하지 않음
			}
		}

		// 둘 다 존재해야 "Descriptors AND Phenomena" 의미 충족
		if (targetDescriptors.isEmpty() || targetPhenomena.isEmpty()) {
			return false;
		}

		// 2) 어떤 Weather 하나라도 "정확히 같은 리스트(순서+길이)"면 존재
		return wg.getWeathers().stream()
			       .anyMatch(w -> w.getDescriptors().equals(targetDescriptors)
				                      && w.getPhenomena().equals(targetPhenomena));
	}

	public static boolean containsDescriptors(Weather w, List<WeatherDescriptor> target, boolean exactMatch) {
		if (exactMatch) return w.getDescriptors().equals(target);
		else return containsOrdered(w.getDescriptors(), target);
	}

	public static boolean containsPhenomena(Weather w, List<WeatherPhenomenon> target, boolean exactMatch) {
		if (exactMatch) return w.getPhenomena().equals(target);
		else return containsOrdered(w.getPhenomena(), target);
	}

	private static <T> boolean containsOrdered(List<T> source, List<T> target) {
		if (target == null) {
			throw new IllegalArgumentException("target must not be null");
		}

		if (target.isEmpty()) {
			return true;
		}

		int targetIndex = 0;
		for (T element : source) {
			if (element.equals(target.get(targetIndex))) {
				targetIndex++;
				if (targetIndex == target.size()) {
					return true;
				}
			} else {
				targetIndex = 0;
			}
		}

		return false;
	}

}
