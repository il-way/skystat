package vo.metar.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RunwaySide {
  LEFT("L"),
  RIGHT("R"),
  CNETER("C"),
  NONE("N");

  private final String symbol;

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
