package com.ilway.skystat.domain.vo.weather.type;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum WeatherDescriptor implements WeatherDescription {
  BC("Patches"),
  BL("Blowing"),
  DR("Drifting"),
  DL("Distant lightning"),
  FZ("Freezing"),
  MI("Shallow"),
  PR("Partial"),
  SH("Showers"),
  TS("Thunderstorm"),
  VC("in the Vicinity");

  private final String description;

  @Override
  public String getDescription() {
    return description;
  }

  public static List<String> names() {
    return Arrays.stream(values())
             .map(Enum::name)
             .toList();
  }

}
