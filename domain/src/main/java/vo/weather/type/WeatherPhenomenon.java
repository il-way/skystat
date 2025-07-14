package vo.weather.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static vo.weather.type.WeatherPhenomenonGroup.*;

@Getter
@RequiredArgsConstructor
public enum WeatherPhenomenon implements WeatherDescription {

  DZ("Drizzle", PRECIPITATION),
  RA("Rain", PRECIPITATION),
  SN("Snow", PRECIPITATION),
  SG("Snow Grains", PRECIPITATION),
  IC("Ice Crystals", PRECIPITATION),
  PL("Ice Pellets", PRECIPITATION),
  GR("Hail", OBSCURATION),
  GS("Small Hail/Snow Pellets", OBSCURATION),
  UP("Unknown Precipitation", OBSCURATION),
  BR("Mist", OBSCURATION),
  FG("Fog", OBSCURATION),
  FU("Smoke", OBSCURATION),
  VA("Volcanic Ash", OBSCURATION),
  DU("Widespread Dust", OBSCURATION),
  SA("Sand", OBSCURATION),
  HZ("Haze", OBSCURATION),
  PY("Spray", OBSCURATION),
  PO("Dust/Sand Whirls", SPECIAL),
  SQ("Squall", SPECIAL),
  FC("Funnel Cloud / Tornado", SPECIAL),
  SS("Sandstorm", SPECIAL),
  DS("Duststorm", SPECIAL),
  WS("Wind Shear", SPECIAL),
  NSW("Nil Significant Weather", SIGNIFICANT);

  private final String description;
  private final WeatherPhenomenonGroup group;

  @Override
  public String getDescription() {
    return description;
  }

  public static List<String> names() {
    return Arrays.stream(values())
             .map(Enum::name)
             .toList();
  }

}
