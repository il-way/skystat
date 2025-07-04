package model.weather;

import vo.weather.type.WeatherDescription;

public record CloudCondition(
  CloudConditionPredicate predicate,
  WeatherDescription  target
) {}
