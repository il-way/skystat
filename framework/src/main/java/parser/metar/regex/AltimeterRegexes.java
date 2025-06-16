package parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import policy.rounding.RoundingPolicy;
import vo.unit.PressureUnit;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum AltimeterRegexes {
  HECTO_PASCAL("hPa", getHectoPascalRegex()),
  INCH_OF_MERCURY("inHg", getInchOfMercuryRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(?:^|\\s)(%s)(?=(?:\\s|$))",
            Arrays.stream(AltimeterRegexes.values())
                    .map(AltimeterRegexes::getRegex)
                    .collect(Collectors.joining("|"))
    );
  }

  private static String getHectoPascalRegex() {
    return "Q(?<hPa>\\d{4})";
  }

  private static String getInchOfMercuryRegex() {
    return "A(?<inHg>\\d{4})";
  }

  public double toValue(String strValue) {
    return switch (this) {
      case HECTO_PASCAL -> Double.parseDouble(strValue);
      case INCH_OF_MERCURY -> Double.parseDouble(strValue) / 100;
      default -> throw new IllegalArgumentException("Invalid altimeter type: " + this);
    };
  }

  public double toHectoPascal(String strValue, RoundingPolicy policy) {
    return switch (this) {
      case HECTO_PASCAL -> Double.parseDouble(strValue);
      case INCH_OF_MERCURY -> {
        double hPa = PressureUnit.INHG.convertTo(Double.parseDouble(strValue) / 100, PressureUnit.HPA);
        yield policy.apply(hPa);
      }
      default -> throw new IllegalArgumentException("Invalid altimeter type: " + this);
    };
  }

  public PressureUnit getUnit() {
    return switch (this) {
      case HECTO_PASCAL -> PressureUnit.HPA;
      case INCH_OF_MERCURY -> PressureUnit.INHG;
      default -> throw new IllegalArgumentException("Invalid pressure unit: " + this);
    };
  }
}
