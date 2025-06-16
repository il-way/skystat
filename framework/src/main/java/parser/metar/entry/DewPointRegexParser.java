package parser.metar.entry;

import parser.metar.regex.TemperaturePairRegexes;
import parser.shared.ReportRegexParser;
import vo.metar.field.Temperature;
import vo.metar.type.MetarField;
import vo.unit.TemperatureUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class DewPointRegexParser extends ReportRegexParser<Temperature> {

  private static final MetarField FIELD_TYPE= MetarField.DEW_POINT;
  private static final String TEMPERATURE_PAIR_REGEX = TemperaturePairRegexes.fullPattern();

  @Override
  public Temperature parse(String rawText) {
    Matcher matcher = getMatcher(rawText, TEMPERATURE_PAIR_REGEX);

    if (!check(matcher)) {
      throw new IllegalArgumentException("TemperaturePair not found in report: " + rawText);
    }

    Map<String, Temperature> temperatureMap = new HashMap<>();
    for (TemperaturePairRegexes type : TemperaturePairRegexes.values()) {
      String match = matcher.group(type.getGroupName());

      if (match == null || match.isEmpty()) {
        throw new IllegalArgumentException("Temperature must always exist as a pair: " + rawText);
      }

      temperatureMap.put(
        type.getGroupName(),
        Temperature.of(type.toCelsius(match), TemperatureUnit.CELSIUS)
      );
    }

    String key = TemperaturePairRegexes.DEW_POINT.getGroupName();
    return temperatureMap.get(key);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}
