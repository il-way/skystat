package model.weather;

import vo.weather.type.WeatherPhenomenon;

import java.util.List;

public record WeatherConditionPredicate(
  WeatherGroupField       field,
  List<WeatherPhenomenon> target
) {}
