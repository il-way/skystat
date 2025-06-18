package model;

import vo.metar.type.WeatherPhenomenon;

import java.util.List;

public record PredicateWeatherCondition(
  MetarWeatherGroupField    field,
  List<WeatherPhenomenon>   target
) {}
