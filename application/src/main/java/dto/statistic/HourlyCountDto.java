package dto.statistic;

import java.time.YearMonth;

public record HourlyCountDto(
  YearMonth  yearMonth,
  int        hourUtc,
  long       days
) {}
