package policy.crosswind;

import exception.GenericPolicyException;
import vo.metar.field.Runway;
import vo.metar.field.RunwayEnd;
import vo.metar.field.Wind;

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