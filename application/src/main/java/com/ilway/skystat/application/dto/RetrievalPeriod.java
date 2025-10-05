package com.ilway.skystat.application.dto;

import com.ilway.skystat.domain.service.TimeOperation;

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

  public static RetrievalPeriod of(int year, int interval) {
    return new RetrievalPeriod(
      TimeOperation.ofLenientUtc(year, 1, 1, 0, 0),
      TimeOperation.ofLenientUtc(year+interval, 1, 1, 0, 0)
    );
  }

}
