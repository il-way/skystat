package policy.modifier;

import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;

public class BasicOverridePolicy implements OverridePolicy {

	@Override
	public WeatherSnapshot overrideWith(WeatherSnapshot base, WeatherSnapshot patch, Modifier modifier) {
		return WeatherSnapshot.builder()
			       .timestamp(base.getTimestamp())
			       .sourceModifier(modifier)
			       .wind(patch.getWind() != null ? patch.getWind() : base.getWind())
			       .visibility(patch.getVisibility() != null ? patch.getVisibility() : base.getVisibility())
			       .weatherGroup(patch.getWeatherGroup() != null && patch.getWeatherGroup().size() != 0
				                     ? patch.getWeatherGroup()
				                     : base.getWeatherGroup())
			       .cloudGroup(patch.getCloudGroup() != null && patch.getCloudGroup().size() != 0
				                   ? patch.getCloudGroup()
				                   : base.getCloudGroup())
			       .temperature(patch.getTemperature() != null ? patch.getTemperature() : base.getTemperature())
			       .altimeter(patch.getAltimeter() != null ? patch.getAltimeter() : base.getAltimeter())
			       .build();
	}

}
