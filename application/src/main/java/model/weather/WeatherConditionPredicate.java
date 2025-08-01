package model.weather;

import lombok.RequiredArgsConstructor;
import service.WeatherOperation;
import vo.weather.WeatherGroup;
import vo.weather.type.WeatherDescription;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherPhenomenon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum WeatherConditionPredicate {

  HAS_PHENOMENA((wg, target) ->
                  WeatherOperation.containsPhenomena(wg, cast(target, WeatherPhenomenon.class))),

  HAS_DESCRIPTORS((wg, target) ->
                   WeatherOperation.containsDescriptors(wg, cast(target, WeatherDescriptor.class))),

  HAS_DESCRIPTORS_AND_PHENOMENA((wg, target) ->
	                  WeatherOperation.containsDescriptors(wg, extract(target, WeatherDescriptor.class))
                 && WeatherOperation.containsPhenomena(wg, extract(target, WeatherPhenomenon.class)));

  private final BiPredicate<WeatherGroup, List<WeatherDescription>> tester;

  public boolean test(WeatherGroup wg, List<WeatherDescription> target ) {
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

  private static <T> List<T> extract(List<WeatherDescription> src, Class<T> clazz) {
    return src.stream()
             .filter(clazz::isInstance)
             .map(clazz::cast)
             .toList();
  }

}
