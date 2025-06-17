package model;

import vo.metar.type.Describable;

import java.util.List;

public record PredicateCondition(
  MetarGroupField   field,
  List<Describable> targetList
) {}
