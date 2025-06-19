package model;

import vo.metar.type.WeatherPhenomenon;

import java.util.List;

public record WeatherConditionPredicate(
  MetarWeatherGroupField    field,
  List<WeatherPhenomenon>   target
) {}
