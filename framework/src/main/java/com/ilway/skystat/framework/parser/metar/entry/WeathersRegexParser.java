package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.WeatherRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class WeathersRegexParser extends ReportRegexParser<Weathers> {

  private static final MetarField FIELD_TYPE= MetarField.WEATHERS;
  private static final String WEATHER_REGEX = WeatherRegexes.fullPattern();

  @Override
  public Weathers parse(String rawText) {
    Matcher matcher = getMatcher(rawText, WEATHER_REGEX);
    WeatherRegexParser weatherParser = new WeatherRegexParser();

    List<Weather> weathers = new ArrayList<>();
    while (matcher.find()) {
      String matchedWeatherText = matcher.group(0);
      Weather weather = weatherParser.parse(matchedWeatherText);
      if (weather != null) {
        weathers.add(weather);
      }
    }

    return Weathers.builder()
            .weathers(weathers)
            .build();
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}