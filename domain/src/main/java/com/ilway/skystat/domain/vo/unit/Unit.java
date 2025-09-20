package com.ilway.skystat.domain.vo.unit;

public interface Unit {

  double toBase(double value);
  double fromBase(double baseValue);

  default double convertTo(double value, Unit targetUnit) {
    double base = this.toBase(value);
    return targetUnit.fromBase(base);
  }

}
