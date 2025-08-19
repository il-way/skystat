package com.ilway.skystat.domain.vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LengthUnit implements Unit {
  METERS(1.0),
  FEET(3.28084),
  MILE(0.000621371);

  private final double toMeterFactor;

  @Override
  public double toBase(double value) {
    return value * toMeterFactor;
  }

  @Override
  public double fromBase(double baseValue) {
    return baseValue / toMeterFactor;
  }
}
