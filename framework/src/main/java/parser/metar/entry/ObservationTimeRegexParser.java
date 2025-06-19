package parser.metar.entry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import parser.metar.regex.ObservationTimeRegexes;
import parser.shared.ReportRegexParser;
import vo.metar.type.MetarField;

import java.time.*;
import java.util.regex.Matcher;

@RequiredArgsConstructor
public class ObservationTimeRegexParser extends ReportRegexParser<ZonedDateTime> {

  private static final MetarField FIELD_TYPE= MetarField.OBSERVATION_TIME;
  private static final String TIME_REGEX = ObservationTimeRegexes.fullPattern();

  @Getter
  private final YearMonth yearMonth;

  @Override
  public ZonedDateTime parse(String rawText) {
    Matcher matcher = getMatcher(rawText, TIME_REGEX);

    if (!check(matcher)) {
      throw new IllegalArgumentException("Observation time not found in report:  " + rawText);
    }

    int day = Integer.parseInt(matcher.group(ObservationTimeRegexes.DAY.getGroupName()));
    int hour = Integer.parseInt(matcher.group(ObservationTimeRegexes.HOUR.getGroupName()));
    int minute = Integer.parseInt(matcher.group(ObservationTimeRegexes.MINUTE.getGroupName()));

    LocalDateTime local = LocalDateTime.of(
            yearMonth.getYear(),
            yearMonth.getMonthValue(),
            day,
            hour,
            minute
    );

    return ZonedDateTime.of(local, ZoneOffset.UTC);
  }

  public ObservationTimeRegexParser withYearMonth(YearMonth yearMonth) {
    return new ObservationTimeRegexParser(yearMonth);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}
