package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherInensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.List;

@Value
public class Weather {

  private final List<WeatherDescriptor> descriptors;
  private final List<WeatherPhenomenon> phenomena;
  private final WeatherInensity intensity;

  @Builder
  public Weather(WeatherInensity intensity, List<WeatherDescriptor> descriptors, List<WeatherPhenomenon> phenomena) {
    this.intensity = intensity;
    this.descriptors = descriptors != null ? List.copyOf(descriptors) : List.of();
    this.phenomena = phenomena != null ? List.copyOf(phenomena) : List.of();
  }

  public static Weather of(WeatherInensity intensity, List<WeatherDescriptor> descriptors, List<WeatherPhenomenon> phenomena) {
    return Weather.builder()
            .intensity(intensity)
            .descriptors(descriptors)
            .phenomena(phenomena)
            .build();
  }

}
