package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.WeatherRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.WeatherGroup;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class WeatherGroupRegexParser extends ReportRegexParser<WeatherGroup> {

  private static final MetarField FIELD_TYPE= MetarField.WEATHER_GROUP;
  private static final String WEATHER_REGEX = WeatherRegexes.fullPattern();

  @Override
  public WeatherGroup parse(String rawText) {
    Matcher matcher = getMatcher(rawText, WEATHER_REGEX);
    WeatherRegexParser weatherParser = new WeatherRegexParser();

    List<Weather> weatherList = new ArrayList<>();
    while (matcher.find()) {
      String matchedWeatherText = matcher.group(0);
      Weather weather = weatherParser.parse(matchedWeatherText);
      weatherList.add(weather);
    }

    return WeatherGroup.builder()
            .weatherList(weatherList)
            .build();
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}