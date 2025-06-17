package vo.metar.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WindDirectionType implements Symbolizable {
  FIXED("FIX"),
  VARIABLE("VRB");

  private final String symbol;

  @Override
  public String getSymbol() {
    return symbol;
  }

}
