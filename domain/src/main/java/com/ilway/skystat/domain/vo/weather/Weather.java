package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherIntensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.List;

@Value
public class Weather {

  private final WeatherIntensity intensity;
  private final List<WeatherDescriptor> descriptors;
  private final List<WeatherPhenomenon> phenomena;

  @Builder
  public Weather(WeatherIntensity intensity, List<WeatherDescriptor> descriptors, List<WeatherPhenomenon> phenomena) {
    this.intensity = intensity;
    this.descriptors = descriptors != null ? List.copyOf(descriptors) : List.of();
    this.phenomena = phenomena != null ? List.copyOf(phenomena) : List.of();
  }

  public static Weather of(WeatherIntensity intensity, List<WeatherDescriptor> descriptors, List<WeatherPhenomenon> phenomena) {
    return Weather.builder()
            .intensity(intensity)
            .descriptors(descriptors)
            .phenomena(phenomena)
            .build();
  }

}
