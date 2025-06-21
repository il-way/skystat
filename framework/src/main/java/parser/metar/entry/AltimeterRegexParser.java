package parser.metar.entry;

import parser.metar.regex.AltimeterRegexes;
import parser.shared.ReportRegexParser;
import policy.rounding.RoundingPolicy;
import vo.weather.Altimeter;
import vo.metar.MetarField;
import vo.unit.PressureUnit;

import java.math.RoundingMode;
import java.util.regex.Matcher;

public class AltimeterRegexParser extends ReportRegexParser<Altimeter> {

  private static final MetarField FIELD_TYPE= MetarField.ALTIMETER;
  private static final String ALTIMETER_REGEX = AltimeterRegexes.fullPattern();
  private static final RoundingPolicy roundingPolicy = RoundingPolicy.of(0, RoundingMode.HALF_UP);

  @Override
  public Altimeter parse(String rawText) {
    Matcher matcher = getMatcher(rawText, ALTIMETER_REGEX);
    if (!check(matcher)) {
      throw new IllegalArgumentException("Altimeter not found in report: " + rawText);
    }

    double altimeter = -1;
    PressureUnit unit = PressureUnit.HPA;
    for (AltimeterRegexes type: AltimeterRegexes.values()) {
      String match = matcher.group(type.getGroupName());

      if (match == null || match.isEmpty())
        continue;

      altimeter = type.toValue(match);
      unit = type.getUnit();
      break;
    }

    if (altimeter < 0) {
      throw new IllegalArgumentException("Altimeter not found in report: " + rawText);
    }

    return Altimeter.of(altimeter, unit);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}