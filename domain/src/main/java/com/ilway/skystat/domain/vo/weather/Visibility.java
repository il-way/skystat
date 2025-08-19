package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.vo.unit.LengthUnit;

@Value
@Builder
public class Visibility {

  private final double value;
  private final LengthUnit unit;

  public static Visibility of(double visibility, LengthUnit unit) {
    return Visibility.builder()
            .value(visibility)
            .unit(unit)
            .build();
  }

  public boolean isAtLeast(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) >= threshold;
  }

  public boolean isAtMost(double threshold, LengthUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) <= threshold;
  }

}
