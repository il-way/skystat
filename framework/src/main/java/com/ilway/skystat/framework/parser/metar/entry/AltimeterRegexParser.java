package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.AltimeterRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.domain.vo.weather.Altimeter;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.unit.PressureUnit;

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