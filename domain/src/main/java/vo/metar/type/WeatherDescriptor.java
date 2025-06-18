package vo.metar.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WeatherDescriptor implements MetarDescription {
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

  @Override
  public String getDescription() {
    return description;
  }

}
