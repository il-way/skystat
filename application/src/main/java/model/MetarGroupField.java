package model;

import lombok.RequiredArgsConstructor;
import vo.metar.Metar;
import vo.metar.type.Describable;
import vo.metar.type.WeatherPhenomenon;

import java.util.List;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum MetarGroupField {

  HAS_PHENOMENA((m,list) -> m.getWeatherGroup().containsPhenomena(list)),
  HAS_CLOUDTYPE((m, list) -> m.getCloudGroup().containsCloudType(list));

  private final BiPredicate<Metar, List<Describable>> tester;

  public boolean test(Metar metar, List<Describable> targetList ) {
    return tester.test(metar, targetList );
  }

}
