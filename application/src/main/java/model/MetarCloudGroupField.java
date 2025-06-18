package model;

import lombok.RequiredArgsConstructor;
import vo.metar.Metar;
import vo.metar.type.CloudCoverage;
import vo.metar.type.CloudType;
import vo.metar.type.MetarDescription;
import vo.metar.type.WeatherPhenomenon;

import java.util.List;
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

  private final BiPredicate<Metar, MetarDescription> tester;

  public boolean test(Metar metar, MetarDescription target) {
    return tester.test(metar, target);
  }

}
