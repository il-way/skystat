package parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import policy.rounding.RoundingPolicy;
import vo.unit.LengthUnit;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum VisibilityRegexes {

  DIGIT("digit","(?<digit>\\d{2,4})"),
  CAVOK("cavok","(?<cavok>CAVOK)"),
  P6SM("p6sm","(?<p6sm>P6SM)"),
  MILE("mile","(?<mile>\\d+SM)"),
  FRACTION_MILE("fractionMile","(?<fractionMile>[\\d\\/]+SM)"),
  IMPROPER_FRACTION_MILE("improperFractionMile", "(?<improperFractionMile>\\d+\\s[\\d\\/]+SM)");

  private final String groupName;
  private final String regex;

  public String getRegex() {
    return regex;
  }

  public String getGroupName() {
    return groupName;
  }

  public static String fullPattern() {
    return String.format("((?:^|\\s))(%s)(?=(?:\\s|$))",
            Arrays.stream(VisibilityRegexes.values())
                    .map(VisibilityRegexes::getRegex)
                    .collect(Collectors.joining("|"))
    );
  }

  public double toValue(String strValue) {
    switch (this) {
      case DIGIT:
        return Double.parseDouble(strValue);
      case CAVOK:
        return 9999;
      case P6SM:
        return 6;
      case MILE: {
        String numeric = strValue.replaceAll("[^\\d]", ""); // SM 제거하고 숫자만
        return Double.parseDouble(numeric);
      }
      case FRACTION_MILE: {
        String[] parts = strValue.replace("SM", "").split("/");
        return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
      }
      case IMPROPER_FRACTION_MILE: {
        String[] parts = strValue.replace("SM", "").trim().split("\\s+"); // "5 1/2"
        int whole = Integer.parseInt(parts[0]);
        String[] fraction = parts[1].split("/");
        double numerator = Double.parseDouble(fraction[0]);
        double denominator = Double.parseDouble(fraction[1]);
        return whole + (numerator / denominator);
      }
      default:
        throw new IllegalArgumentException("Invalid visibility type: " + this);
    }
  }

  public double toMeters(String strValue, RoundingPolicy policy) {
    switch (this) {
      case DIGIT:
        return Double.parseDouble(strValue);
      case CAVOK:
        return 9999;
      case P6SM:
        return 9999;
      case MILE: {
        String numeric = strValue.replaceAll("[^\\d]", ""); // SM 제거하고 숫자만
        double miles = Double.parseDouble(numeric);
        return policy.apply(LengthUnit.MILE.convertTo(miles, LengthUnit.METERS));
      }
      case FRACTION_MILE: {
        String[] parts = strValue.replace("SM", "").split("/");
        double miles = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        return policy.apply(LengthUnit.MILE.convertTo(miles, LengthUnit.METERS));
      }
      case IMPROPER_FRACTION_MILE: {
        String[] parts = strValue.replace("SM", "").trim().split("\\s+"); // "5 1/2"
        int whole = Integer.parseInt(parts[0]);
        String[] fraction = parts[1].split("/");
        double numerator = Double.parseDouble(fraction[0]);
        double denominator = Double.parseDouble(fraction[1]);
        double miles = whole + (numerator / denominator);
        return policy.apply(LengthUnit.MILE.convertTo(miles, LengthUnit.METERS));
      }
      default:
        throw new IllegalArgumentException("Invalid visibility type: " + this);
    }
  }

  public LengthUnit getUnit() {
    return switch (this) {
      case DIGIT, CAVOK -> LengthUnit.METERS;
      case P6SM, MILE, FRACTION_MILE, IMPROPER_FRACTION_MILE -> LengthUnit.MILE;
      default -> throw new IllegalArgumentException("Invalid visibility unit: " + this);
    };
  }
}
