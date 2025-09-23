package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
@Builder
public class Weathers {

  @Singular("weather")
  private final List<Weather> weathers;

  public Weathers(List<Weather> weathers) {
    this.weathers = weathers != null ? List.copyOf(weathers) : List.of();
  }

  public static Weathers of(List<Weather> weathers) {
    return Weathers.builder()
             .weathers(weathers)
             .build();
  }

  public static Weathers of(Weather... weathers) {
    return Weathers.builder()
             .weathers(Arrays.stream(weathers).toList())
             .build();
  }

  public static Weathers ofEmpty() {
    return Weathers.builder()
             .weathers(List.of())
             .build();
  }

  public int size() {
    return weathers.size();
  }

}

