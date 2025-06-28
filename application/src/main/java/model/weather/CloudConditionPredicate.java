package model.weather;

import vo.weather.type.WeatherDescription;

public record CloudConditionPredicate(
  CloudGroupField     field,
  WeatherDescription  target
) {}
