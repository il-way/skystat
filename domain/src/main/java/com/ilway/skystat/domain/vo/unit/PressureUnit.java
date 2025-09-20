package com.ilway.skystat.domain.vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PressureUnit implements Unit {
  HPA(1.0),
  INHG(33.8639);

  private final double toHpaFactor;

  @Override
  public double toBase(double value) {
    return value * toHpaFactor;
  }

  @Override
  public double fromBase(double baseValue) {
    return baseValue / toHpaFactor;
  }

}
