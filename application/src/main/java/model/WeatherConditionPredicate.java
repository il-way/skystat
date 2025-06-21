package model;

import vo.weather.type.WeatherPhenomenon;

import java.util.List;

public record WeatherConditionPredicate(
  MetarWeatherGroupField    field,
  List<WeatherPhenomenon>   target
) {}
