package com.ilway.skystat.domain.vo.weather.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CloudType implements WeatherDescription {

  TCU("Towering Cumulus"),
  CB("Cumulonimbus"),
  NONE("None");

  private final String description;

  @Override
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
