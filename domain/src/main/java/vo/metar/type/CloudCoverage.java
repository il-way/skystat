package vo.metar.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CloudCoverage {

  FEW("Few"),
  SCT("Scattered"),
  BKN("Broken"),
  OVC("Overcast"),
  VV("Vertical visibility"),
  SKC("Sky clear"),
  CLR("Clear"),
  NSC("No significant cloud"),
  NCD("No cloud detected");

  private final String description;

  public String getDescription() {
    return description;
  }

  public boolean requiresAltitude() {
    return switch (this) {
      case FEW, SCT, BKN, OVC, VV -> true;
      default -> false;
    };
  }

}
