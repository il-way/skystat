package com.ilway.skystat.domain.service;

import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.WeatherGroup;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.List;

public class WeatherOperation {

	public static boolean contains(WeatherGroup wg, String target) {
		return wg.getWeatherList().stream()
			       .anyMatch(w -> contains(w, target));
	}

	public static boolean containsPhenomena(WeatherGroup wg, List<WeatherPhenomenon> target) {
		return wg.getWeatherList().stream()
			       .anyMatch(w -> containsPhenomena(w, target));
	}

	public static boolean containsDescriptors(WeatherGroup wg, List<WeatherDescriptor> target) {
		return wg.getWeatherList().stream()
			.anyMatch(w -> containsDescriptors(w, target));
	}

	public static boolean contains(Weather w, String target) {
		List<WeatherPhenomenon> targetPhenomena = new ArrayList<>();
		List<WeatherDescriptor> targetDescriptors = new ArrayList<>();

		for (int idx=0; idx<target.length(); idx+=2) {
			String targetCode = target.substring(2 * idx, 2 * (idx + 1));
			if (WeatherPhenomenon.names().contains(targetCode)) {
				targetPhenomena.add(WeatherPhenomenon.valueOf(targetCode));
				continue;
			}

			if (WeatherDescriptor.names().contains(targetCode)) {
				targetDescriptors.add(WeatherDescriptor.valueOf(targetCode));
			}
		}

		return containsDescriptors(w, targetDescriptors) && containsPhenomena(w, targetPhenomena);
	}

	public static boolean containsDescriptors(Weather w, List<WeatherDescriptor> target) {
		return containsOrdered(w.getDescriptors(), target);
	}

	public static boolean containsPhenomena(Weather w, List<WeatherPhenomenon> target) {
		return containsOrdered(w.getPhenomena(), target);
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
