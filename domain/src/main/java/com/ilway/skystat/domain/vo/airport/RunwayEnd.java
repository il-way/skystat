package com.ilway.skystat.domain.vo.airport;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.ilway.skystat.domain.spec.RunwayHeadingSpec;
import com.ilway.skystat.domain.vo.airport.type.RunwaySide;

@Getter
@EqualsAndHashCode
public class RunwayEnd {

  private final Integer heading;
  private final RunwaySide side;
  private final boolean available;

  private static final RunwayHeadingSpec headingSpec = new RunwayHeadingSpec();

  @Builder
  public RunwayEnd(Integer heading, RunwaySide side, boolean available) {
    headingSpec.check(heading);

    this.heading = heading;
    this.side = side;
    this.available = available;
  }

  public static RunwayEnd of(Integer heading, RunwaySide side, boolean available) {
    return RunwayEnd.builder()
            .heading(heading)
            .side(side)
            .available(available)
            .build();
  }

  public String getDesignator() {
    return String.format("%02d%s", heading, side);
  }

  public String getNumberOnly() {
    return String.format("%02d", heading);
  }

  public boolean isAvailable() {
    return available;
  }

}
