package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class WeatherGroup {

  private final List<Weather> weatherList;

  @Builder
  public WeatherGroup(List<Weather> weatherList) {
    this.weatherList = weatherList != null ? List.copyOf(weatherList) : List.of();
  }

  public static WeatherGroup of(List<Weather> weatherList) {
    return WeatherGroup.builder()
            .weatherList(weatherList)
            .build();
  }

  public static WeatherGroup of(Weather... weathers) {
    return WeatherGroup.builder()
             .weatherList(Arrays.stream(weathers).toList())
             .build();
  }

  public static WeatherGroup ofEmpty() {
    return WeatherGroup.builder()
            .weatherList(List.of())
            .build();
  }

  public int size() {
    return weatherList.size();
  }

}
