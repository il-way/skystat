package vo.metar.field;

import lombok.Builder;
import lombok.Value;
import vo.metar.type.WeatherDescriptor;
import vo.metar.type.WeatherInensity;
import vo.metar.type.WeatherPhenomenon;

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
      List<WeatherPhenomenon> targetList = IntStream
              .range(0, target.length() / 2)
              .mapToObj(idx -> target.substring(idx * 2, 2 * (idx + 1)))
              .map(WeatherPhenomenon::valueOf)
              .collect(Collectors.toList());

      return containsPhenomena(targetList);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public boolean containsPhenomena(List<WeatherPhenomenon> targetList) {
    if (targetList.isEmpty())
      return true;

    int targetIndex = 0;
    for (WeatherPhenomenon p : this.phenomena) {
      if (p.equals(targetList.get(targetIndex))) {
        targetIndex++;
      }

      if (targetIndex == targetList.size()) {
        return true;
      }
    }

    return false;
  }

}
