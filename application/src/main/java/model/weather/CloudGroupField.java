package model.weather;

import lombok.RequiredArgsConstructor;
import vo.weather.CloudGroup;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;
import vo.weather.type.WeatherDescription;

import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum CloudGroupField {

  HAS_CLOUDTYPE((cg, target) -> {
    if (!(target instanceof CloudType)) {
      throw new IllegalArgumentException("target must be CloudType, but: " + target.getClass().getSimpleName());
    }
    return cg.containsCloudType((CloudType) target);
  }),

  HAS_COVERAGE((cg, target) -> {
    if (!(target instanceof CloudCoverage)) {
      throw new IllegalArgumentException("target must be CloudCoverage, but: " + target.getClass().getSimpleName());
    }
    return cg.containsCloudCoverage((CloudCoverage) target);
  });

  private final BiPredicate<CloudGroup, WeatherDescription> tester;

  public boolean test(CloudGroup cg, WeatherDescription target) {
    return tester.test(cg, target);
  }

}
