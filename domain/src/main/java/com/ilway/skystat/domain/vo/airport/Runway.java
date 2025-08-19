package com.ilway.skystat.domain.vo.airport;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.ilway.skystat.domain.vo.unit.LengthUnit;

@Getter
@Builder
@EqualsAndHashCode
public class Runway {

  private final RunwayEnd endA;
  private final RunwayEnd endB;
  private final double length;
  private final LengthUnit unit;

  public static Runway of(RunwayEnd endA, RunwayEnd endB, double length, LengthUnit unit) {
    return Runway.builder()
        .endA(endA)
        .endB(endB)
        .length(length)
        .unit(unit)
        .build();
  }

  public boolean isAtMost(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(length, targetUnit) <= threshold;
  }

  public boolean isLessThan(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(length, targetUnit) < threshold;
  }

  public boolean isAtLeast(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(length, targetUnit) >= threshold;
  }

  public boolean isGreaterThan(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(length, targetUnit) > threshold;
  }

  public String getName() {
    return endA.getDesignator() + "/" + endB.getDesignator();
  }

  public String getNameWithoutSide() {
    return endA.getNumberOnly() + "/" + endB.getNumberOnly();
  }

}
