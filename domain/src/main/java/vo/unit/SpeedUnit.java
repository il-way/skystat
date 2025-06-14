package vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpeedUnit implements Unit {

  MPS(1.0, "MPS"),
  KT(1.94384, "KT"),
  KPH(3.6, "KPH");

  private final double toMpsFactor;
  private final String symbol;

  public String getSymbol() {
    return symbol;
  }

  @Override
  public double toBase(double value) {
    return value * toMpsFactor;
  }

  @Override
  public double fromBase(double baseValue) {
    return baseValue / toMpsFactor;
  }

}
