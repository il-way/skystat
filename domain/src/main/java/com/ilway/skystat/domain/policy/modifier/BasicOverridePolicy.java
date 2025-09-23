package com.ilway.skystat.domain.policy.modifier;

import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.taf.type.Modifier;

public class BasicOverridePolicy implements OverridePolicy {

	@Override
	public WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier) {
		return WeatherSnapshot.builder()
			       .timestamp(base.getTimestamp())
			       .sourceModifier(modifier)
			       .wind(patch.getWind() != null ? patch.getWind() : base.getWind())
			       .visibility(patch.getVisibility() != null ? patch.getVisibility() : base.getVisibility())
			       .weathers(patch.getWeathers() != null && patch.getWeathers().size() != 0
				                     ? patch.getWeathers()
				                     : base.getWeathers())
			       .clouds(patch.getClouds() != null && patch.getClouds().size() != 0
				                   ? patch.getClouds()
				                   : base.getClouds())
			       .temperature(patch.getTemperature() != null ? patch.getTemperature() : base.getTemperature())
			       .altimeter(patch.getAltimeter() != null ? patch.getAltimeter() : base.getAltimeter())
			       .build();
	}

}
