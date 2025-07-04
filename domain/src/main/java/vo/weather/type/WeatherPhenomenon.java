package vo.weather.type;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum WeatherPhenomenon implements WeatherDescription {
  DZ("Drizzle"),
  RA("Rain"),
  SN("Snow"),
  SG("Snow Grains"),
  IC("Ice Crystals"),
  PL("Ice Pellets"),
  GR("Hail"),
  GS("Small Hail/Snow Pellets"),
  UP("Unknown Precipitation"),
  BR("Mist"),
  FG("Fog"),
  FU("Smoke"),
  VA("Volcanic Ash"),
  DU("Widespread Dust"),
  SA("Sand"),
  HZ("Haze"),
  PY("Spray"),
  PO("Dust/Sand Whirls"),
  SQ("Squall"),
  FC("Funnel Cloud / Tornado"),
  SS("Sandstorm"),
  DS("Duststorm"),
  WS("Wind Shear"),
  NSW("Nil Significant Weather");

  private final String description;

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
