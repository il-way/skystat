package vo.weather.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WindDirectionType implements WeatherSymbol {
  FIXED("FIX"),
  VARIABLE("VRB");

  private final String symbol;

  @Override
  public String getSymbol() {
    return symbol;
  }

}
