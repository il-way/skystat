package com.ilway.skystat.domain.vo.taf.field;

import com.ilway.skystat.domain.vo.weather.*;
import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.policy.modifier.BasicOverridePolicy;
import com.ilway.skystat.domain.policy.modifier.FromOverridePolicy;
import com.ilway.skystat.domain.policy.modifier.TempoOverridePolicy;
import com.ilway.skystat.domain.vo.taf.type.Modifier;

import java.time.ZonedDateTime;

@Value
public class WeatherSnapshot implements MetricSource {

	// Basic Elements(Optional)
	private final Wind wind;
	private final Visibility visibility;
	private final Weathers weathers;
	private final Clouds clouds;

	// Optional
	private final Temperature temperature;
	private final Altimeter altimeter;

	// Supplementary Elements
	private final ZonedDateTime timestamp;
	private final Modifier sourceModifier;

	@Builder
	public WeatherSnapshot(Wind wind, Visibility visibility, Weathers weathers, Clouds clouds, Temperature temperature, Altimeter altimeter, ZonedDateTime timestamp, Modifier sourceModifier) {
		this.wind = wind;
		this.visibility = visibility;
		this.weathers = (weathers == null)
			                    ? Weathers.ofEmpty()
			                    : Weathers.of(weathers.getWeathers());
		this.clouds = clouds == null
			                  ? Clouds.ofEmpty()
			                  : Clouds.of(clouds.getClouds());
		this.temperature = temperature;
		this.altimeter = altimeter;
		this.timestamp = timestamp;
		this.sourceModifier = sourceModifier;
	}

	public static WeatherSnapshot empty(ZonedDateTime ts) {
		return WeatherSnapshot.builder()
			       .timestamp(ts)
			       .weathers(Weathers.ofEmpty())
			       .clouds(Clouds.ofEmpty())
			       .sourceModifier(Modifier.NONE)
			       .build();
	}

	public WeatherSnapshot overrideWith(WeatherSnapshot patch, Modifier modifier) {
		return switch (modifier) {
			case FM
				-> new FromOverridePolicy().overrideWith(this, patch, modifier);
			case HEADER, BECMG, NONE
				-> new BasicOverridePolicy().overrideWith(this, patch, modifier);
			case TEMPO, PROB30_TEMPO, PROB40_TEMPO, PROB30, PROB40
				-> new TempoOverridePolicy().overrideWith(this, patch, modifier);
		};
	}

}
