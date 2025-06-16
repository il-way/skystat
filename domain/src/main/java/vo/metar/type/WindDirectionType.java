package vo.metar.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WindDirectionType {
  FIXED("FIX"),
  VARIABLE("VRB");

  private final String symbol;

}
