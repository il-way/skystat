package com.ilway.skystat.framework.parser;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.ObservationTimeRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObservataionTimeTest {

  ReportParser<ZonedDateTime> parser = new ObservationTimeRegexParser(YearMonth.of(2025, 5));

  @Test
  public void 관측시간_파싱_성공() {
    String metar = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070 13/13 Q1007 NOSIG";

    ZonedDateTime result = new ObservationTimeRegexParser(YearMonth.of(2025, 5))
        .parse(metar);

    assertEquals(result, ZonedDateTime.of(
        LocalDateTime.of(2025, 5, 1, 3, 0),
      ZoneOffset.UTC));
  }

}
