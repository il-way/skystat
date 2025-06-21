package vo.taf.field;

import lombok.Builder;
import lombok.Value;
import policy.modifier.BasicOverridePolicy;
import policy.modifier.FmOverridePolicy;
import vo.taf.type.Modifier;
import vo.weather.*;

import java.time.ZonedDateTime;

@Value
@Builder
public class WeatherSnapshot {

	// Basic Elements
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

	public static WeatherSnapshot empty(ZonedDateTime ts) {
		return WeatherSnapshot.builder()
			       .timestamp(ts)
			       .sourceModifier(Modifier.NONE)
			       .build();
	}

	public WeatherSnapshot overrideWith(WeatherSnapshot patch, Modifier modifier) {
		return switch (modifier) {
			case FM -> new FmOverridePolicy().overrideWith(this, patch, modifier);
			case HEADER, BECMG, TEMPO, PROB30_TEMPO, PROB40_TEMPO,
			     PROB30, PROB40, NONE
				-> new BasicOverridePolicy().overrideWith(this, patch, modifier);
		};
	}

}
