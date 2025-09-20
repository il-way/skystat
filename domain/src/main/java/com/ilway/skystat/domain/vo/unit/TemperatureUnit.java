package com.ilway.skystat.domain.vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TemperatureUnit implements Unit {

  CELSIUS(1.0),
  FAHRENHEIT(0.555555555);

  private final double toCelsiusFactor;

  @Override
  public double toBase(double value) {
    return switch (this) {
      case CELSIUS -> value;
      case FAHRENHEIT -> (value - 32) * toCelsiusFactor;
    };
  }

  @Override
  public double fromBase(double baseValue) {
    return switch (this) {
      case CELSIUS -> baseValue;
      case FAHRENHEIT -> (baseValue / toCelsiusFactor) + 32;
    };
  }

}
