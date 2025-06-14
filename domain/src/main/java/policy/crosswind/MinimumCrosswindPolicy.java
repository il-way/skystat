package policy.crosswind;

import java.util.List;

import vo.metar.field.Wind;
import vo.metar.field.Runway;

public interface MinimumCrosswindPolicy {
  
  double calculate(Wind wind, List<Runway> runways);

}
