package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.WindRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.WindDirection;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import java.util.regex.Matcher;

public class WindRegexParser extends ReportRegexParser<Wind> {

  private static final MetarField FIELD_TYPE = MetarField.WIND;
  private static final String WIND_REGEX = WindRegexes.fullPattern();

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

    WindDirection direction = windDirection.equals(WindDirectionType.VARIABLE.getSymbol())
            ? WindDirection.variable()
            : WindDirection.fixed(Double.valueOf(windDirection));

    double windSpeedValue = Double.parseDouble(windSpeed);
    double windGustsValue = windGusts != null ? Double.parseDouble(windGusts) : 0;
    SpeedUnit speedUnit = windUnit.equals(SpeedUnit.KT.getSymbol()) ? SpeedUnit.KT : SpeedUnit.MPS;

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
}