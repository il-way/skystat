package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.vo.unit.PressureUnit;

@Value
@Builder
public class Altimeter {

  private final double value;
  private final PressureUnit unit;

  public static Altimeter of(double value, PressureUnit unit) {
    return Altimeter.builder()
            .value(value)
            .unit(unit)
            .build();
  }

  public boolean isAtMost(double threshold, PressureUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) <= threshold;
  }

  public boolean isAtLeast(double threshold, PressureUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) >= threshold;
  }

}
