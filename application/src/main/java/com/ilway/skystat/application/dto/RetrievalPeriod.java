package com.ilway.skystat.application.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public record RetrievalPeriod(ZonedDateTime fromInclusive, ZonedDateTime toExclusive) {

  public RetrievalPeriod {
    Objects.requireNonNull(fromInclusive, "Time must not be nuil");
    Objects.requireNonNull(toExclusive, "Time must not be nuil");

    if (!fromInclusive.isBefore(toExclusive)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }

}
