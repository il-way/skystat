package com.ilway.skystat.application.model.weather;

import lombok.RequiredArgsConstructor;
import com.ilway.skystat.domain.service.CloudOperation;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;

import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum CloudConditionPredicate {

  HAS_CLOUDTYPE((cg, target) -> {
    return CloudOperation.containsCloudType(cg, cast(target, CloudType.class));
  }),

  HAS_COVERAGE((cg, target) -> {
    return CloudOperation.containsCloudCoverage(cg, cast(target, CloudCoverage.class));
  });

  private final BiPredicate<Clouds, WeatherDescription> tester;

  public boolean test(Clouds cg, WeatherDescription target) {
    return tester.test(cg, target);
  }

  private static <T> T cast(WeatherDescription src, Class<T> clazz) {
    if (!clazz.isInstance(src)) {
      throw new IllegalArgumentException("target must be "
                                           + clazz.getSimpleName()
                                           + ", but: "
                                           + src.getClass().getSimpleName());
    }

    return clazz.cast(src);
  }

}
