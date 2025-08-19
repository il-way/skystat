package com.ilway.skystat.domain.policy.crosswind;

import java.util.List;

import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.airport.Runway;

public interface MinimumCrosswindPolicy {
  
  double calculate(Wind wind, List<Runway> runways);

}
