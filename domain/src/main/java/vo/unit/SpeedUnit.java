package vo.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpeedUnit implements Unit {

  MPS(1.0, "MPS"),
  KT(1.94384, "KT"),
  KPH(3.6, "KPH");

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
