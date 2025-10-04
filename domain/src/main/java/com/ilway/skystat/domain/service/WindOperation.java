package com.ilway.skystat.domain.service;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import com.ilway.skystat.domain.policy.crosswind.MinimumCrosswindPolicy;
import com.ilway.skystat.domain.policy.crosswind.MinimumCrosswindPolicyType;
import com.ilway.skystat.domain.policy.crosswind.MultiRunwayMinimumCrosswindPolicy;
import com.ilway.skystat.domain.policy.crosswind.SingleRunwayMinimumCrosswindPolicy;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.domain.vo.airport.Runway;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.unit.LengthUnit;

@AllArgsConstructor
public class WindOperation {

  private RoundingPolicy roundingPolicy;
  private Map<MinimumCrosswindPolicyType, MinimumCrosswindPolicy> policyMap;

  public WindOperation(double runwayThreshold, LengthUnit unit) {
    this.roundingPolicy = RoundingPolicy.of(1, RoundingMode.HALF_UP);
    this.policyMap = Map.of(
      MinimumCrosswindPolicyType.MULTI_RUNWAY, new MultiRunwayMinimumCrosswindPolicy(runwayThreshold, unit),
      MinimumCrosswindPolicyType.SINGLE_RUNWAY, new SingleRunwayMinimumCrosswindPolicy()
    );
  }

  public double minimumCrosswind(Wind wind, List<Runway> runways, MinimumCrosswindPolicyType policyType) {
    if(policyType == null) {
      throw new IllegalArgumentException("Unsupported policy type: " + policyType);
    }
    
    return roundingPolicy.apply(policyMap.get(policyType).calculate(wind, runways));
  }

  public static double getWindPeak(Wind wind) {
    return Math.max(wind.getSpeed(), wind.getGusts());
  }

}