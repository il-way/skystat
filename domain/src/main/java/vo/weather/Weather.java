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
    this.descriptor = descriptor;
    this.phenomena = List.copyOf(phenomena);
  }

  public static Weather of(WeatherInensity intensity, List<WeatherDescriptor> descriptor, List<WeatherPhenomenon> phenomena) {
    return Weather.builder()
            .intensity(intensity)
            .descriptor(descriptor)
            .phenomena(phenomena)
            .build();
  }

  public boolean contains(String target) {
    List<WeatherPhenomenon> targetPhenomena = new ArrayList<>();
    List<WeatherDescriptor> targetDescriptors = new ArrayList<>();

    for (int idx=0; idx<target.length(); idx++) {
      String targetCode = target.substring(2 * idx, 2 * (idx + 1));
      if (WeatherPhenomenon.names().contains(targetCode)) {
        targetPhenomena.add(WeatherPhenomenon.valueOf(targetCode));
        continue;
      }

      if (WeatherDescriptor.names().contains(targetCode)) {
        targetDescriptors.add(WeatherDescriptor.valueOf(targetCode));
      }
    }

    return containsDescriptors(targetDescriptors) && containsPhenomena(targetPhenomena);
  }

  public boolean containsPhenomena(List<WeatherPhenomenon> target) {
    return containsOrdered(this.phenomena, target);
  }

  public boolean containsDescriptors(List<WeatherDescriptor> target) {
    return containsOrdered(this.descriptor, target);
  }

  private <T> boolean containsOrdered(List<T> source, List<T> target) {
    if (target == null) {
      throw new IllegalArgumentException("target mut not be null");
    }
    
    if (target.isEmpty()) {
      return true;
    }

    int targetIndex = 0;
    for (T element : source) {
      if (element.equals(target.get(targetIndex))) {
        targetIndex++;
        if (targetIndex == target.size()) {
          return true;
        }
      } else {
        targetIndex = 0;
      }
    }

    return false;
  }

}
