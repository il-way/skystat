package parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemperaturePairRegexes {
  TEMPERATURE("temperature", getTemperatureRegex()),
  DEW_POINT("dewPoint", getDewpointRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(?:^|\\s)(%s)\\/(%s)(?=(?:\\s|$))",
            getTemperatureRegex(),
            getDewpointRegex()
    );
  }

  private static String getTemperatureRegex() {
    return "(?<temperature>M?\\d{2})";
  }

  private static String getDewpointRegex() {
    return "(?<dewPoint>M?\\d{2})";
  }

  public double toCelsius(String strValue) {
    return switch (this) {
      case TEMPERATURE, DEW_POINT -> strValue.startsWith("M")
              ? -Double.parseDouble(strValue.substring(1))
              : Double.parseDouble(strValue);
      default -> throw new IllegalArgumentException("Invalid temperature type: " + this);
    };
  }
}
