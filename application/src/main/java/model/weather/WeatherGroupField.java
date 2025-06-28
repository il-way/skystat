package model.weather;

import lombok.RequiredArgsConstructor;
import vo.weather.WeatherGroup;
import vo.weather.type.WeatherPhenomenon;

import java.util.List;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum WeatherGroupField {

  HAS_PHENOMENA(WeatherGroup::containsPhenomena);

  private final BiPredicate<WeatherGroup, List<WeatherPhenomenon>> tester;

  public boolean test(WeatherGroup wg, List<WeatherPhenomenon> target ) {
    return tester.test(wg, target );
  }

}
