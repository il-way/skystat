package dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public record MetarRetrievalPeriod(ZonedDateTime from, ZonedDateTime to) {

  public MetarRetrievalPeriod {
    Objects.requireNonNull(from, "Time must not be nuil");
    Objects.requireNonNull(to, "Time must not be nuil");

    if (!from.isBefore(to)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }

}
