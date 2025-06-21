package model;

import lombok.RequiredArgsConstructor;
import vo.metar.Metar;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;
import vo.weather.type.WeatherDescription;

import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum MetarCloudGroupField {

  HAS_CLOUDTYPE((m, target) -> {
    if (!(target instanceof CloudType)) {
      throw new IllegalArgumentException("target must be CloudType, but: " + target.getClass().getSimpleName());
    }
    return m.getCloudGroup().containsCloudType((CloudType) target);
  }),

  HAS_COVERAGE((m, target) -> {
    if (!(target instanceof CloudCoverage)) {
      throw new IllegalArgumentException("target must be CloudCoverage, but: " + target.getClass().getSimpleName());
    }
    return m.getCloudGroup().containsCloudCoverage((CloudCoverage) target);
  });

  private final BiPredicate<Metar, WeatherDescription> tester;

  public boolean test(Metar metar, WeatherDescription target) {
    return tester.test(metar, target);
  }

}
