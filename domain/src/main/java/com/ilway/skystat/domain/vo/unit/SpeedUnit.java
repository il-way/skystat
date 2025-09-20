package com.ilway.skystat.domain.vo.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpeedUnit implements Unit {

  MPS(1.0, "MPS"),
  KT(0.51444, "KT"),
  KPH(0.2778, "KPH");

  private final double toMpsFactor;

  @Getter
  private final String symbol;

	@Override
  public double toBase(double value) {
    return value * toMpsFactor;
  }

  @Override
  public double fromBase(double baseValue) {
    return baseValue / toMpsFactor;
  }

}
