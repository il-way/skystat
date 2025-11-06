package com.ilway.skystat.domain.policy.modifier;

import com.ilway.skystat.domain.service.WeatherOperation;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.taf.type.Modifier;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenonGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TempoOverridePolicy implements OverridePolicy {

	@Override
	public WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier) {
		return WeatherSnapshot.builder()
			       .timestamp(base.getTimestamp())
			       .sourceModifier(modifier)
			       .wind(patch.getWind() != null ? patch.getWind() : base.getWind())
			       .visibility(patch.getVisibility() != null ? patch.getVisibility() : base.getVisibility())
			       .weathers(patch.getWeathers() != null && patch.getWeathers().size() != 0
				                     ? overrideWeatherGroup(base.getWeathers(), patch.getWeathers())
				                     : base.getWeathers())
			       .clouds(patch.getClouds() != null && patch.getClouds().size() != 0
				                   ? patch.getClouds()
				                   : base.getClouds())
			       .temperature(patch.getTemperature() != null ? patch.getTemperature() : base.getTemperature())
			       .altimeter(patch.getAltimeter() != null ? patch.getAltimeter() : base.getAltimeter())
			       .build();
	}

	private Weathers overrideWeatherGroup(Weathers base, Weathers patch) {
		if (base.size() == 0) return patch;
		if (WeatherOperation.containsPhenomena(patch, List.of(WeatherPhenomenon.NSW), false)) return patch;

		List<Weather> merged         = new ArrayList<>();
		List<Weather> remainingPatch = new ArrayList<>(patch.getWeathers());

		for (Weather bw : base.getWeathers()) {
			Weather replacement = remainingPatch.stream()
				                      .filter(pw -> groupsEqual(bw.getPhenomena(), pw.getPhenomena()))
				                      .findFirst()
				                      .orElse(null);

			if (replacement != null) {
				merged.add(replacement);
				remainingPatch.remove(replacement);
			} else {
				merged.add(bw);
			}
		}

		merged.addAll(remainingPatch);
		return Weathers.of(merged);
	}

	private boolean groupsEqual(List<WeatherPhenomenon> w1, List<WeatherPhenomenon> w2) {
		Set<WeatherPhenomenonGroup> groupSet1 = w1.stream()
			                                        .map(WeatherPhenomenon::getGroup)
			                                        .collect(Collectors.toSet());

		Set<WeatherPhenomenonGroup> groupSet2 = w2.stream()
			                                        .map(WeatherPhenomenon::getGroup)
			                                        .collect(Collectors.toSet());

		return groupSet1.equals(groupSet2);
	}

}
