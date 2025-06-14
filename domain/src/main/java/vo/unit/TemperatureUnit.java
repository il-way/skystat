package vo.unit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TemperatureUnit implements Unit {

  CELSIUS(1.0),
  FAHRENHEIT(1.8);

  private final double toCelsiusFactor;

  @Override
  public double toBase(double value) {
    return switch (this) {
      case CELSIUS -> value;
      case FAHRENHEIT -> value * toCelsiusFactor + 32;
    };
  }

  @Override
  public double fromBase(double baseValue) {
    return switch (this) {
      case CELSIUS -> baseValue;
      case FAHRENHEIT -> (baseValue - 32) / toCelsiusFactor;
    };
  }

}
