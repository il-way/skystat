package model;

import vo.metar.type.MetarDescription;

import java.util.List;

public record PredicateCloudCondition(
  MetarCloudGroupField  field,
  MetarDescription      target
) {}
