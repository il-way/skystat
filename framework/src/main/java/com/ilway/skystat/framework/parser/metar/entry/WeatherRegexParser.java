package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.WeatherRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherInensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;

public class WeatherRegexParser extends ReportRegexParser<Weather> {

  private static final MetarField FIELD_TYPE = MetarField.WEATHER;
  private static final String WEATHER_REGEX = WeatherRegexes.fullPattern();

  @Override
  public Weather parse(String rawText) {
    Matcher matcher = getMatcher(rawText, WEATHER_REGEX);

    if (!check(matcher)) {
      return null;
    }

    String intensityMatch = matcher.group(WeatherRegexes.INTENSITY.getGroupName());
    WeatherInensity intensity = intensityMatch != null
            ? WeatherInensity.fromSymbol(intensityMatch)
            : WeatherInensity.MODERATE;

    List<WeatherDescriptor> descriptor = parseTokens(
      matcher.group(WeatherRegexes.DESCRIPTOR.getGroupName()),
      WeatherRegexes.DESCRIPTOR.getRegex(),
      WeatherDescriptor::valueOf
    );

    List<WeatherPhenomenon> phenomena = parseTokens(
      matcher.group(WeatherRegexes.PHENOMENON.getGroupName()),
      WeatherRegexes.PHENOMENON.getRegex(),
      WeatherPhenomenon::valueOf
    );

    if (descriptor.isEmpty() && phenomena.isEmpty()) {
      return null;
    }

    return Weather.builder()
            .intensity(intensity)
            .descriptors(descriptor)
            .phenomena(phenomena)
            .build();
  }

  private <T> List<T> parseTokens(String matchedPart, String regex, Function<String, T> converter) {
    if (matchedPart == null || matchedPart.isBlank()) {
      return List.of();
    }

    List<T> result = new ArrayList<>();
    Matcher matcher = getMatcher(matchedPart, regex);
    while (matcher.find()) {
      result.add(converter.apply(matcher.group(0)));
    }

    return result;
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}