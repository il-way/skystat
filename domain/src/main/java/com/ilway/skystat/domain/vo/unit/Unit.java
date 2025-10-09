package com.ilway.skystat.domain.vo.unit;

import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;

public interface Unit {

  double toBase(double value);
  double fromBase(double baseValue);

  default double convertTo(double value, Unit targetUnit) {
    double base = this.toBase(value);
    return targetUnit.fromBase(base);
  }

  default double convertTo(double value, Unit targetUnit, RoundingPolicy policy) {
    double base = this.toBase(value);
    return policy.apply(targetUnit.fromBase(base));
  }

}
