package parser.metar.entry;

import parser.metar.regex.WeatherRegexes;
import parser.shared.ReportRegexParser;
import vo.weather.Weather;
import vo.metar.MetarField;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherInensity;
import vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class WeatherRegexParser extends ReportRegexParser<Weather> {

  private static final MetarField FIELD_TYPE = MetarField.WEATHER;
  private static final String WEATHER_REGEX = WeatherRegexes.fullPattern();
  private static final String PHENOMENON_REGEX = WeatherRegexes.PHENOMENON.getRegex();
  private static final String DESCRIPTOR_REGEX = WeatherRegexes.DESCRIPTOR.getRegex();

  @Override
  public Weather parse(String rawText) {
    Matcher matcher = getMatcher(rawText, WEATHER_REGEX);

    if (!check(matcher)) {
      return null;
    }

    String intensityMatch = matcher.group(WeatherRegexes.INTENSITY.getGroupName());
    String descriptorMatch = matcher.group(WeatherRegexes.DESCRIPTOR.getGroupName());
    String phenomenonMatch = matcher.group(WeatherRegexes.PHENOMENON.getGroupName());

    WeatherInensity intensity = intensityMatch != null
            ? WeatherInensity.fromSymbol(intensityMatch)
            : WeatherInensity.MODERATE;

    List<WeatherDescriptor> descriptor = parseDescriptors(descriptorMatch, DESCRIPTOR_REGEX);
    List<WeatherPhenomenon> phenomena = parsePhenomena(phenomenonMatch, PHENOMENON_REGEX);

    if (descriptor.isEmpty() && phenomena.isEmpty()) {
      return null;
    }

    return Weather.builder()
            .intensity(intensity)
            .descriptor(descriptor)
            .phenomena(phenomena)
            .build();
  }

  private List<WeatherPhenomenon> parsePhenomena(String phenomenonMatch, String phenomenonRegex) {
    List<WeatherPhenomenon> phenomena = new ArrayList<>();
    if (phenomenonMatch == null || phenomenonMatch.isBlank()) {
      return phenomena;
    }

    Matcher matcher = getMatcher(phenomenonMatch, phenomenonRegex);

    while (matcher.find()) {
      String matched = matcher.group(0);
      WeatherPhenomenon phenomenon = WeatherPhenomenon.valueOf(matched);
      phenomena.add(phenomenon);
    }

    return phenomena;
  }

  private List<WeatherDescriptor> parseDescriptors(String descriptorMatch, String descriptorRegex) {
    List<WeatherDescriptor> descriptors = new ArrayList<>();
    if (descriptorMatch == null || descriptorMatch.isBlank()) {
      return descriptors;
    }

    Matcher matcher = getMatcher(descriptorMatch, descriptorRegex);

    while (matcher.find()) {
      String matched = matcher.group(0);
      WeatherDescriptor descriptor = WeatherDescriptor.valueOf(matched);
      descriptors.add(descriptor);
    }

    return descriptors;
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}