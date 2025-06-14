package vo.metar.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WeatherDescriptor {
  BC("Patches"),
  BL("Blowing"),
  DR("Drifting"),
  DL("Distant lightning"),
  FZ("Freezing"),
  MI("Shallow"),
  PR("Partial"),
  SH("Showers"),
  VC("in the Vicinity");

  private final String description;

  public String getDescription() {
    return description;
  }
}
