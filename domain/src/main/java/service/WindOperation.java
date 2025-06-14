package service;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import policy.crosswind.MinimumCrosswindPolicy;
import policy.crosswind.MinimumCrosswindPolicyType;
import policy.crosswind.MultiRunwayMinimumCrosswindPolicy;
import policy.crosswind.SingleRunwayMinimumCrosswindPolicy;
import policy.rounding.RoundingPolicy;
import vo.metar.field.Runway;
import vo.metar.field.Wind;
import vo.unit.LengthUnit;

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

}