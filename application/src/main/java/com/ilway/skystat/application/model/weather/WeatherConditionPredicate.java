package com.ilway.skystat.application.model.weather;

import lombok.RequiredArgsConstructor;
import com.ilway.skystat.domain.service.WeatherOperation;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static com.ilway.skystat.domain.service.WeatherOperation.*;
import static com.ilway.skystat.domain.service.WeatherOperation.containsDescriptors;

@RequiredArgsConstructor
public enum WeatherConditionPredicate {

  HAS_PHENOMENA((wg, target) ->
                  containsPhenomena(wg, cast(target, WeatherPhenomenon.class), false)),

  HAS_DESCRIPTORS((wg, target) ->
                   containsDescriptors(wg, cast(target, WeatherDescriptor.class), false)),

  HAS_DESCRIPTORS_AND_PHENOMENA((wg, target) -> WeatherOperation.containsDescriptorsAndPhenomena(wg, target, false));

  private final BiPredicate<Weathers, List<WeatherDescription>> tester;

  public boolean test(Weathers wg, List<WeatherDescription> target) {
    return tester.test(wg, target);
  }

  private static <T> List<T> cast(List<WeatherDescription> src, Class<T> clazz) {
    List<T> out = new ArrayList<>();
    for (WeatherDescription o: src) {
      if (!clazz.isInstance(o)) {
        throw new IllegalArgumentException("target must be"
                                             + clazz.getSimpleName()
                                             + ", but: "
                                             + o.getClass().getSimpleName());
      }
      out.add(clazz.cast(o));
    }

	  return out;
  }

  public static <T> List<T> extract(List<WeatherDescription> src, Class<T> clazz) {
    return src.stream()
             .filter(clazz::isInstance)
             .map(clazz::cast)
             .toList();
  }

}
