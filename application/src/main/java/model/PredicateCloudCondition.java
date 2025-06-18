package model;

import vo.metar.type.Describable;

import java.util.List;

public record PredicateCloudCondition(
  MetarCloudGroupField  field,
  Describable           target
) {}
