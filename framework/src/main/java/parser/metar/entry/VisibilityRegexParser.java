package parser.metar.entry;

import parser.metar.regex.VisibilityRegexes;
import parser.shared.ReportRegexParser;
import policy.rounding.RoundingPolicy;
import vo.weather.Visibility;
import vo.metar.MetarField;
import vo.unit.LengthUnit;

import java.math.RoundingMode;
import java.util.regex.Matcher;

public class VisibilityRegexParser extends ReportRegexParser<Visibility> {

  private static final MetarField FIELD_TYPE= MetarField.VISIBILITY;
  private static final String VISIBILITY_REGEX = VisibilityRegexes.fullPattern();
  private static final RoundingPolicy policy = RoundingPolicy.of(0, RoundingMode.HALF_UP);

  @Override
  public Visibility parse(String rawText) {
    Matcher matcher = getMatcher(rawText, VISIBILITY_REGEX);

    if (!check(matcher)) {
      throw new IllegalArgumentException("Visibility not found in report: " + rawText);
    }

    double visibility = -1;
    LengthUnit unit = LengthUnit.METERS;
    for (VisibilityRegexes type : VisibilityRegexes.values()) {
      String match = matcher.group(type.getGroupName());

      if (match == null || match.isEmpty())
        continue;

      visibility = type.toValue(match);
      unit = type.getUnit();
      break;
    }

    if (visibility < 0) {
      throw new IllegalArgumentException("Visibility not found in report: " + rawText);
    }

    return Visibility.of(visibility, unit);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}