package com.ilway.skystat.domain.policy.modifier;

import com.ilway.skystat.domain.exception.GenericPolicyException;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.taf.type.Modifier;

public class FromOverridePolicy implements OverridePolicy {

	@Override
	public WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier) {
		if (modifier != Modifier.FM) {
			throw new GenericPolicyException("modifier must be FM, but found: " + modifier.name());
		}

		return WeatherSnapshot.builder()
			       .timestamp(base.getTimestamp())
			       .sourceModifier(Modifier.FM)
			       .wind(patch.getWind())
			       .visibility(patch.getVisibility())
			       .weathers(patch.getWeathers())
			       .clouds(patch.getClouds())
			       .temperature(patch.getTemperature())
			       .altimeter(patch.getAltimeter())
			       .build();
	}

}
