package policy.modifier;

import exception.GenericPolicyException;
import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;

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
			       .weatherGroup(patch.getWeatherGroup())
			       .cloudGroup(patch.getCloudGroup())
			       .temperature(patch.getTemperature())
			       .altimeter(patch.getAltimeter())
			       .build();
	}

}
