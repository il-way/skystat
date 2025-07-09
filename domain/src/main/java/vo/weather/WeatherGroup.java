package vo.weather;

import lombok.Builder;
import lombok.Value;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherPhenomenon;

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

  public static WeatherGroup ofEmpty() {
    return WeatherGroup.builder()
            .weatherList(List.of())
            .build();
  }

  public boolean contains(String target) {
    return weatherList.stream()
             .anyMatch(weather -> weather.contains(target));
  }

  public boolean containsDescriptors(List<WeatherDescriptor> target) {
    return weatherList.stream()
            .anyMatch(weather -> weather.containsDescriptors(target));
  }

  public boolean containsPhenomena(List<WeatherPhenomenon> target) {
    return weatherList.stream()
            .anyMatch(weather -> weather.containsPhenomena(target));
  }

  public int size() {
    return weatherList.size();
  }

}
