package com.ilway.skystat.application.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public record RetrievalPeriod(ZonedDateTime from, ZonedDateTime to) {

  public RetrievalPeriod {
    Objects.requireNonNull(from, "Time must not be nuil");
    Objects.requireNonNull(to, "Time must not be nuil");

    if (!from.isBefore(to)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }

}
