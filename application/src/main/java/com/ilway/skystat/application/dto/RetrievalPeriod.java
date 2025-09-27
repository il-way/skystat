package com.ilway.skystat.application.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public record RetrievalPeriod(ZonedDateTime fromInclusive, ZonedDateTime toEsclusive) {

  public RetrievalPeriod {
    Objects.requireNonNull(fromInclusive, "Time must not be nuil");
    Objects.requireNonNull(toEsclusive, "Time must not be nuil");

    if (!fromInclusive.isBefore(toEsclusive)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }

}
