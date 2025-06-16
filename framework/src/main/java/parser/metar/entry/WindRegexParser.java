package parser.metar.entry;

import parser.metar.regex.WindRegexes;
import parser.shared.ReportRegexParser;
import vo.metar.field.Wind;
import vo.metar.field.WindDirection;
import vo.metar.type.MetarField;
import vo.metar.type.WindDirectionType;
import vo.unit.SpeedUnit;

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