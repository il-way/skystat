package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherInensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum WeatherRegexes {
  INTENSITY("intensity", getIntensityRegex()),
  DESCRIPTOR("descriptor", genDescriptorRegex()),
  PHENOMENON("phenomenon", genPhenomenonRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format(
            "(?:^|\\s)" +
            "(?<%1$s>%2$s)?" +
            "(?<%3$s>(%4$s){1,3})?" +
            "(?<%5$s>(?:%6$s){1,3})" +
            "(?=(?:\\s|$))",
            INTENSITY.groupName, INTENSITY.regex,
            DESCRIPTOR.groupName, DESCRIPTOR.regex,
            PHENOMENON.groupName, PHENOMENON.regex
    );
  }

  private static String getIntensityRegex() {
    return Arrays.stream(WeatherInensity.values())
        .map(WeatherInensity::getSymbol)
        .filter(symbol -> !symbol.isEmpty())
        .map(Pattern::quote)
        .collect(Collectors.joining("|"));
  }

  private static String genDescriptorRegex() {
    return Arrays.stream(WeatherDescriptor.values())
        .map(Enum::name)
        .collect(Collectors.joining("|"));
  }

  private static String genPhenomenonRegex() {
    return Arrays.stream(WeatherPhenomenon.values())
        .map(Enum::name)
        .collect(Collectors.joining("|"));
  }
}
