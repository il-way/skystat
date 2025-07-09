package vo.airport.type;

import lombok.RequiredArgsConstructor;
import vo.weather.type.WeatherSymbol;

@RequiredArgsConstructor
public enum RunwaySide implements WeatherSymbol {
  LEFT("L"),
  RIGHT("R"),
  CENTER("C"),
  NONE("N");

  private final String symbol;

  @Override
  public String getSymbol() {
    return symbol;
  }

  public static RunwaySide fromCode(String symbol) {
    for (RunwaySide side : RunwaySide.values()) {
      if (side.getSymbol().equalsIgnoreCase(symbol)) return side;
    }
    throw new IllegalArgumentException("Invalid runway side symbol: " + symbol);
  }
}
