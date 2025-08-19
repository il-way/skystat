package com.ilway.skystat.application.model.weather;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;

public record CloudCondition(
  CloudConditionPredicate predicate,
  WeatherDescription  target
) {}
