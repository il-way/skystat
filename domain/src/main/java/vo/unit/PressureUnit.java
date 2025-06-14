package vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PressureUnit implements Unit {
  HPA(1.0),
  INHG(0.02953);

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
