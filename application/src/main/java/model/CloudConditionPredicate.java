package model;

import vo.metar.type.MetarDescription;

import java.util.List;

public record CloudConditionPredicate(
  MetarCloudGroupField  field,
  MetarDescription      target
) {}
