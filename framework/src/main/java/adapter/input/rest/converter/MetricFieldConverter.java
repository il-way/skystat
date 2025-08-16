package adapter.input.rest.converter;

import model.weather.MetricField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricFieldConverter implements Converter<String, MetricField> {
	@Override
	public MetricField convert(String source) {
		String s = source.trim().toLowerCase();
		return switch (s) {
			case "ceiling" -> MetricField.LOWEST_CEILING;
			case "visibility" -> MetricField.VISIBILITY;
			case "peakwind" -> MetricField.PEAK_WIND;
			case "windspeed" -> MetricField.WIND_SPEED;
			case "altimeter" -> MetricField.ALTIMETER;
			default -> throw new IllegalArgumentException("Unsupported field: " + s);
		};
	}
}
