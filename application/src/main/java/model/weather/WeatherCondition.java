package model.weather;

import vo.weather.type.WeatherDescription;

import java.util.List;

public record WeatherCondition(
  WeatherConditionPredicate predicate,
  List<WeatherDescription> target
) {}
