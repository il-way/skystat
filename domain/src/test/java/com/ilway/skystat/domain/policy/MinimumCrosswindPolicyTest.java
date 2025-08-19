package com.ilway.skystat.domain.policy;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.domain.service.WindOperation;
import com.ilway.skystat.domain.vo.airport.Runway;
import com.ilway.skystat.domain.vo.airport.RunwayEnd;
import com.ilway.skystat.domain.vo.airport.type.RunwaySide;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.WindDirection;

import java.util.List;

import static com.ilway.skystat.domain.policy.crosswind.MinimumCrosswindPolicyType.MULTI_RUNWAY;
import static com.ilway.skystat.domain.policy.crosswind.MinimumCrosswindPolicyType.SINGLE_RUNWAY;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimumCrosswindPolicyTest {

  Runway singleRunway1 = Runway.of(
          RunwayEnd.of(34, RunwaySide.NONE, true),
          RunwayEnd.of(16, RunwaySide.NONE, true),
          8000,
          FEET);

  Runway singleRunway2 = Runway.of(
          RunwayEnd.of(33, RunwaySide.NONE, true),
          RunwayEnd.of(15, RunwaySide.NONE, true),
          8000,
          FEET);

  Runway shortRunway = Runway.of(
          RunwayEnd.of(34, RunwaySide.NONE, true),
          RunwayEnd.of(16, RunwaySide.NONE, true),
          13_000,
          FEET);

  Runway longRunway = Runway.of(
          RunwayEnd.of(33, RunwaySide.NONE, true),
          RunwayEnd.of(15, RunwaySide.NONE, true),
          7_000,
          FEET);

  WindOperation wo = new WindOperation(8000, FEET);

  @Test
  void 단일활주로의_측풍최소치_정책_연산에_성공해야한다() {
    Wind wind = Wind.of(WindDirection.fixed(100), 20, 40, KT);

    // when
    double expected = 34.6;
    double actual = wo.minimumCrosswind(wind, List.of(singleRunway1), SINGLE_RUNWAY);

    assertEquals(expected, actual);
  }

  @Test
  void 복수활주로의_측풍최소치_정책_연산에_성공해야한다() {
    Wind wind = Wind.of(WindDirection.fixed(100), 20, 40, KT);

    double expected = 30.6;
    double actual = wo.minimumCrosswind(wind, List.of(singleRunway1, singleRunway2), MULTI_RUNWAY);

    assertEquals(expected, actual);
  }

  @Test
  void 복수활주로에서_활주로길이가_임계치_이하인_활주로는_연산에서_제외돼야한다() {
    Wind wind = Wind.of(WindDirection.fixed(100), 20, 40, KT);

    double expected = 34.6;
    double actual = wo.minimumCrosswind(wind, List.of(shortRunway, longRunway), MULTI_RUNWAY);

    assertEquals(expected, actual);
  }
}
