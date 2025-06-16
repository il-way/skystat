package parser.metar.entry;

import parser.metar.regex.WeatherRegexes;
import parser.shared.ReportRegexParser;
import vo.metar.field.Weather;
import vo.metar.field.WeatherGroup;
import vo.metar.type.MetarField;

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