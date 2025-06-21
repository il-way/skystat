package vo.weather;

import lombok.Builder;
import lombok.Value;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherInensity;
import vo.weather.type.WeatherPhenomenon;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Value
public class Weather {

  private final WeatherDescriptor descriptor;
  private final List<WeatherPhenomenon> phenomena;
  private final WeatherInensity intensity;

  @Builder
  public Weather(WeatherInensity intensity, WeatherDescriptor descriptor, List<WeatherPhenomenon> phenomena) {
    this.intensity = intensity;
    this.descriptor = descriptor;
    this.phenomena = List.copyOf(phenomena);
  }

  public static Weather of(WeatherInensity intensity, WeatherDescriptor descriptor, List<WeatherPhenomenon> phenomena) {
    return Weather.builder()
            .intensity(intensity)
            .descriptor(descriptor)
            .phenomena(phenomena)
            .build();
  }

  public boolean containsPhenomena(String target) {
    try {
      List<WeatherPhenomenon> targetToList = IntStream
              .range(0, target.length() / 2)
              .mapToObj(idx -> target.substring(idx * 2, 2 * (idx + 1)))
              .map(WeatherPhenomenon::valueOf)
              .collect(Collectors.toList());

      return containsPhenomena(targetToList);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public boolean containsPhenomena(List<WeatherPhenomenon> target) {
    if (target == null) {
      throw new IllegalArgumentException("target mut not be null");
    }

    if (target.isEmpty())
      return true;

    int targetIndex = 0;
    for (WeatherPhenomenon p : this.phenomena) {
      if (p.equals(target.get(targetIndex))) {
        targetIndex++;
      }

      if (targetIndex == target.size()) {
        return true;
      }
    }

    return false;
  }

}
