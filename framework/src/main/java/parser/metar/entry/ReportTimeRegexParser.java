package parser.metar.entry;

import lombok.Getter;
import parser.shared.ReportRegexParser;
import service.TimeOperation;
import vo.metar.MetarField;
import vo.metar.ReportType;

import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ReportTimeRegexParser extends ReportRegexParser<ZonedDateTime> {

  private final MetarField FIELD_TYPE = MetarField.REPORT_TIME;

  @Getter
  private final YearMonth yearMonth;
  private final ReportTypeRegexParser reportTypeParser;
  private final ObservationTimeRegexParser obsTimeParser;

  public ReportTimeRegexParser(YearMonth yearMonth) {
    this.yearMonth = yearMonth;
    this.reportTypeParser = new ReportTypeRegexParser();
    this.obsTimeParser = new ObservationTimeRegexParser(yearMonth);
  }

  @Override
  public ZonedDateTime parse(String rawText) {
    ReportType reportType = reportTypeParser.parse(rawText);
    ZonedDateTime obsTime = obsTimeParser.parse(rawText);

    if (reportType == ReportType.METAR) {
      return TimeOperation.toReportTime(obsTime);
    }

    return obsTime;
  }

  private ZonedDateTime roundTo(ZonedDateTime obsTime) {
    ZonedDateTime utc = obsTime.withZoneSameInstant(ZoneOffset.UTC);
    ZonedDateTime baseHour = utc.truncatedTo(ChronoUnit.HOURS);

    int minute = utc.getMinute();
    int second = utc.getSecond();
    int totalSeconds = minute * 60 + second;

    if (totalSeconds < 15 * 60) return baseHour;
    else if (totalSeconds < 45 * 60) return baseHour.plusMinutes(30);
    else return baseHour.plusHours(1);
  }

  public ReportTimeRegexParser withYearMonth(YearMonth newYm) {
    return new ReportTimeRegexParser(newYm);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}