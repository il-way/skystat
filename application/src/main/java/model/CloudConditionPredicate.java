package model;

import vo.weather.type.WeatherDescription;

public record CloudConditionPredicate(
  MetarCloudGroupField  field,
  WeatherDescription target
) {}
