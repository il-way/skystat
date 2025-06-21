package vo.taf.field;

import lombok.Builder;
import lombok.Value;
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

}
