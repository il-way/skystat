package adapter.input.rest.converter;

import model.weather.WeatherConditionPredicate;
import org.springframework.core.convert.converter.Converter;

public class WeatherConditionPredicateConverter implements Converter<String, WeatherConditionPredicate> {
	@Override
	public WeatherConditionPredicate convert(String source) {
		String s = source.trim().toLowerCase();
		return switch (s) {
			case "phenomena" -> WeatherConditionPredicate.HAS_PHENOMENA;
			case "descriptor" -> WeatherConditionPredicate.HAS_DESCRIPTORS;
			case "both" -> WeatherConditionPredicate.HAS_DESCRIPTORS_AND_PHENOMENA;
			default -> throw new IllegalArgumentException("Unsupported field: " + s);
		};
	}
}
