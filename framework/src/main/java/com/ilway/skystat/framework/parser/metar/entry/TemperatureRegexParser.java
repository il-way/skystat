package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.TemperaturePairRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Temperature;
import com.ilway.skystat.domain.vo.metar.MetarField;
import com.ilway.skystat.domain.vo.unit.TemperatureUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class TemperatureRegexParser extends ReportRegexParser<Temperature> {

  private static final MetarField FIELD_TYPE= MetarField.TEMPERATURE;
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

    String key = TemperaturePairRegexes.TEMPERATURE.getGroupName();
    return temperatureMap.get(key);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}
