package com.ilway.skystat.application.model.weather;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;

import java.util.List;

public record WeatherCondition(
  WeatherConditionPredicate predicate,
  List<WeatherDescription> target
) {}
