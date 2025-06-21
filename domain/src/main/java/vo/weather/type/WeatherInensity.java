package vo.weather.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WeatherInensity {
  LIGHT("-"),
  MODERATE(""),
  HEAVY("+");

  private final String symbol;

  public String getSymbol() {
    return symbol;
  }

  public static WeatherInensity fromSymbol(String symbol) {
    for (WeatherInensity intensity : WeatherInensity.values()) {
      if (intensity.getSymbol().equalsIgnoreCase(symbol)) return intensity;
    }
    throw new IllegalArgumentException("Invalid weather intensity symbol: " + symbol);
  }
}
