package policy.crosswind;

import java.util.List;

import vo.weather.Wind;
import vo.airport.Runway;

public interface MinimumCrosswindPolicy {
  
  double calculate(Wind wind, List<Runway> runways);

}
