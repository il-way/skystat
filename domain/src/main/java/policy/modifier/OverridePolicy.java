package policy.modifier;

import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;

public interface OverridePolicy {

	WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier);

}
