package vo.taf.field;

import lombok.Builder;
import lombok.Value;
import policy.modifier.BasicOverridePolicy;
import policy.modifier.FromOverridePolicy;
import policy.modifier.TempoOverridePolicy;
import vo.taf.type.Modifier;
import vo.weather.*;

import java.time.ZonedDateTime;
import java.util.List;

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
			                    : WeatherGroup.of(List.copyOf(weatherGroup.getWeatherList()));
		this.cloudGroup = cloudGroup == null
			                  ? CloudGroup.ofEmpty()
			                  : CloudGroup.of(List.copyOf(cloudGroup.getCloudList()));
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
