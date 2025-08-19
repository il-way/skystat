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
	private final WeatherGroup weatherGroup;
	private final CloudGroup cloudGroup;

	// Optional
	private final Temperature temperature;
	private final Altimeter altimeter;

	// Supplementary Elements
	private final ZonedDateTime timestamp;
	private final Modifier sourceModifier;

	@Builder
	public WeatherSnapshot(Wind wind, Visibility visibility, WeatherGroup weatherGroup, CloudGroup cloudGroup, Temperature temperature, Altimeter altimeter, ZonedDateTime timestamp, Modifier sourceModifier) {
		this.wind = wind;
		this.visibility = visibility;
		this.weatherGroup = (weatherGroup == null)
			                    ? WeatherGroup.ofEmpty()
			                    : WeatherGroup.of(weatherGroup.getWeatherList());
		this.cloudGroup = cloudGroup == null
			                  ? CloudGroup.ofEmpty()
			                  : CloudGroup.of(cloudGroup.getCloudList());
		this.temperature = temperature;
		this.altimeter = altimeter;
		this.timestamp = timestamp;
		this.sourceModifier = sourceModifier;
	}

	public static WeatherSnapshot empty(ZonedDateTime ts) {
		return WeatherSnapshot.builder()
			       .timestamp(ts)
			       .weatherGroup(WeatherGroup.ofEmpty())
			       .cloudGroup(CloudGroup.ofEmpty())
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
