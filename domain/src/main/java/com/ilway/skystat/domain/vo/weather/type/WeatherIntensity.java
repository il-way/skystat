package com.ilway.skystat.domain.vo.weather.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum WeatherIntensity {
  LIGHT("-"),
  MODERATE(""),
  HEAVY("+");

  private final String symbol;

	public static WeatherIntensity fromSymbol(String symbol) {
    for (WeatherIntensity intensity : WeatherIntensity.values()) {
      if (intensity.getSymbol().equalsIgnoreCase(symbol)) return intensity;
    }
    throw new IllegalArgumentException("Invalid weather intensity symbol: " + symbol);
  }

  public static List<String> names() {
    return Arrays.stream(values())
             .map(Enum::name)
             .toList();
  }

}
