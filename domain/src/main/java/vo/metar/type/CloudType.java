package vo.metar.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CloudType {

  TCU("Towering Cumulus"),
  CB("Cumulonimbus"),
  NONE("None");

  private final String description;

  public String getDescription() {
    return description;
  }

  public boolean hasCumulonimbus() {
    return this == CB;
  }

  public boolean hasToweringCumulus() {
    return this == TCU;
  }

}
