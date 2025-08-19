package com.ilway.skystat.domain.policy.crosswind;

import com.ilway.skystat.domain.exception.GenericPolicyException;
import com.ilway.skystat.domain.vo.airport.Runway;
import com.ilway.skystat.domain.vo.airport.RunwayEnd;
import com.ilway.skystat.domain.vo.weather.Wind;

import java.util.List;
import java.util.stream.Stream;

public class SingleRunwayMinimumCrosswindPolicy implements MinimumCrosswindPolicy {

  @Override
  public double calculate(Wind wind, List<Runway> runways) {
    if (runways.size() != 1) {
      throw new GenericPolicyException("Only one runway is allowed for this policy.");
    }

    return runways.stream()
      .flatMap(runway -> Stream.of(runway.getEndA(), runway.getEndB()))
      .filter(RunwayEnd::isAvailable)
      .map(end -> wind.calculateCrosswind(end.getHeading()))
      .mapToDouble(Wind::getPeakSpeed)
      .min()
      .orElseThrow(() -> new GenericPolicyException("No available runway ends for crosswind calculation."));
  }
  
}