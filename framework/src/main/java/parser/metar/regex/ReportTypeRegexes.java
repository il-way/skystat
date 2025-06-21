package parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vo.metar.ReportType;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ReportTypeRegexes {

  TYPE("type", getTypeRegex());

  private final String groupName;
  private final String regex;

  public static String fullPattern() {
    return String.format("(%s)", getTypeRegex());
  }

  private static String getTypeRegex() {
    return Arrays.stream(ReportType.values())
            .map(Enum::name)
            .collect(Collectors.joining("|", "(?<type>", ")"));
  }

}
