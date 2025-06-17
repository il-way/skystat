package vo.metar.field;

import lombok.Builder;
import lombok.Value;
import vo.metar.type.Describable;
import vo.metar.type.WeatherPhenomenon;

import java.util.List;

@Value
public class WeatherGroup {

  private final List<Weather> weatherList;

  @Builder
  public WeatherGroup(List<Weather> weatherList) {
    this.weatherList = List.copyOf(weatherList);
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

  public boolean containsPhenomena(String target) {
    return weatherList.stream()
            .anyMatch(weather -> weather.containsPhenomena(target));
  }

  public boolean containsPhenomena(List<Describable> targetList) {
    return weatherList.stream()
            .anyMatch(weather -> weather.containsPhenomena(targetList));
  }

  public int size() {
    return weatherList.size();
  }

}
