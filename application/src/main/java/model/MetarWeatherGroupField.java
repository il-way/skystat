package model;

import lombok.RequiredArgsConstructor;
import vo.metar.Metar;
import vo.metar.type.Describable;
import vo.metar.type.WeatherPhenomenon;

import java.util.List;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum MetarWeatherGroupField {

  HAS_PHENOMENA((m,list) -> m.getWeatherGroup().containsPhenomena(list));

  private final BiPredicate<Metar, List<WeatherPhenomenon>> tester;

  public boolean test(Metar metar, List<WeatherPhenomenon> target ) {
    return tester.test(metar, target );
  }

}
