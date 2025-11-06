package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.WindRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.WindDirection;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import java.util.regex.Matcher;

import static com.ilway.skystat.domain.vo.unit.SpeedUnit.*;

public class WindRegexParser extends ReportRegexParser<Wind> {

  private static final MetarField FIELD_TYPE = MetarField.WIND;
  private static final String WIND_REGEX = WindRegexes.fullPattern();
  private static final double UPPER_LIMIT_KT = 100;
  private static final double UPPER_LIMIT_MPS = 50;

  @Override
  public Wind parse(String rawText) {
    Matcher matcher = getMatcher(rawText, WIND_REGEX);

    if (!check(matcher)) {
      throw new IllegalArgumentException("Wind not found in report:  " + rawText);
    }

    String windDirection = matcher.group(WindRegexes.DIRECTION.getGroupName());
    String windSpeed = matcher.group(WindRegexes.SPEED.getGroupName());
    String windGusts = matcher.group(WindRegexes.GUSTS.getGroupName());
    String windUnit = matcher.group(WindRegexes.UNIT.getGroupName());

    SpeedUnit speedUnit = valueOf(windUnit);
    WindDirection direction = windDirection.equals(WindDirectionType.VARIABLE.getSymbol())
            ? WindDirection.variable()
            : WindDirection.fixed(Double.parseDouble(windDirection));

    double windSpeedValue = parseSpeed(windSpeed, speedUnit);
    double windGustsValue = parseSpeed(windGusts, speedUnit);



    return Wind.builder()
            .direction(direction)
            .speed(windSpeedValue)
            .gusts(windGustsValue)
            .unit(speedUnit)
            .build();
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

  private double parseSpeed(String speed, SpeedUnit unit) {
    if (speed == null) return 0;
    if (speed.startsWith("P")) {
      if (unit.equals(MPS)) return UPPER_LIMIT_MPS;
      else return UPPER_LIMIT_KT;
    }
    else return Double.parseDouble(speed);
  }

}