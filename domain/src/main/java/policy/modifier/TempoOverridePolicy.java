package policy.modifier;

import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;
import vo.weather.Weather;
import vo.weather.WeatherGroup;
import vo.weather.type.WeatherPhenomenon;
import vo.weather.type.WeatherPhenomenonGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TempoOverridePolicy implements OverridePolicy {

	@Override
	public WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier) {
		return WeatherSnapshot.builder()
			       .timestamp(base.getTimestamp())
			       .sourceModifier(modifier)
			       .wind(patch.getWind() != null ? patch.getWind() : base.getWind())
			       .visibility(patch.getVisibility() != null ? patch.getVisibility() : base.getVisibility())
			       .weatherGroup(patch.getWeatherGroup() != null && patch.getWeatherGroup().size() != 0
				                     ? overrideWeatherGroup(base.getWeatherGroup(), patch.getWeatherGroup())
				                     : base.getWeatherGroup())
			       .cloudGroup(patch.getCloudGroup() != null && patch.getCloudGroup().size() != 0
				                   ? patch.getCloudGroup()
				                   : base.getCloudGroup())
			       .temperature(patch.getTemperature() != null ? patch.getTemperature() : base.getTemperature())
			       .altimeter(patch.getAltimeter() != null ? patch.getAltimeter() : base.getAltimeter())
			       .build();
	}

	private WeatherGroup overrideWeatherGroup(WeatherGroup base, WeatherGroup patch) {
		if (base.size() == 0) return patch;

		List<Weather> merged         = new ArrayList<>();
		List<Weather> remainingPatch = new ArrayList<>(patch.getWeatherList());

		for (Weather bw : base.getWeatherList()) {
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
		return WeatherGroup.of(merged);
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
