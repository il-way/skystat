package com.ilway.skystat.domain.vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LengthUnit implements Unit {
  METERS(1.0),
  FEET(0.3048),
  MILE(1609.344);

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
