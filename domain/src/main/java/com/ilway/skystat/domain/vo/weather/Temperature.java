package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.Value;
import com.ilway.skystat.domain.vo.unit.TemperatureUnit;

@Value
@Builder
public class Temperature {

  private final double value;
  private final TemperatureUnit unit;

  public static Temperature of(double value, TemperatureUnit unit) {
    return Temperature.builder()
            .value(value)
            .unit(unit)
            .build();
  }

  public boolean isAtLeast(double threshold, TemperatureUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) >= threshold;
  }

  public boolean isAtMost(double threshold, TemperatureUnit targetUnit) {
    return this.unit.convertTo(value, targetUnit) <= threshold;
  }

}
