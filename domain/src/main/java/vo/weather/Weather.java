package vo.weather;

import lombok.Builder;
import lombok.Value;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherInensity;
import vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Value
public class Weather {

  private final List<WeatherDescriptor> descriptor;
  private final List<WeatherPhenomenon> phenomena;
  private final WeatherInensity intensity;

  @Builder
  public Weather(WeatherInensity intensity, List<WeatherDescriptor> descriptor, List<WeatherPhenomenon> phenomena) {
    this.intensity = intensity;
    this.descriptor = descriptor != null ? List.copyOf(descriptor) : List.of();
    this.phenomena = phenomena != null ? List.copyOf(phenomena) : List.of();
  }

  public static Weather of(WeatherInensity intensity, List<WeatherDescriptor> descriptor, List<WeatherPhenomenon> phenomena) {
    return Weather.builder()
            .intensity(intensity)
            .descriptor(descriptor)
            .phenomena(phenomena)
            .build();
  }

}
