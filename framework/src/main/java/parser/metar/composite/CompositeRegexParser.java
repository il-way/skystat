package parser.metar.composite;

import lombok.NoArgsConstructor;
import parser.metar.entry.ObservationTimeRegexParser;
import parser.metar.entry.ReportTimeRegexParser;
import parser.shared.ReportParser;
import parser.shared.ReportRegexParser;
import vo.metar.MetarField;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CompositeRegexParser implements ReportParser<Map<MetarField, Object>> {

  private final List<ReportRegexParser<?>> entries = new ArrayList<>();

  @Override
  public Map<MetarField, Object> parse(String rawText) {
    Map<MetarField, Object> parsingResultMap = new HashMap<>();

    for (ReportRegexParser<?> entryParser : entries) {
      parsingResultMap.put(entryParser.getFieldType(), entryParser.parse(rawText));
    }

    return parsingResultMap;
  }

  public void add(ReportRegexParser<?> entryParser) {
    this.entries.add(entryParser);
  }

  public void setYearMonth(YearMonth yearMonth) {
    for (int i=0; i<entries.size(); i++) {
      if (entries.get(i) instanceof ObservationTimeRegexParser otp) {
        entries.set(i, otp.withYearMonth(yearMonth));
      }
      else if (entries.get(i) instanceof ReportTimeRegexParser rtp) {
        entries.set(i, rtp.withYearMonth(yearMonth));
      }
    }
  }

  public YearMonth getYearMonth() {
    return entries.stream()
            .filter(ObservationTimeRegexParser.class::isInstance)
            .map(ObservationTimeRegexParser.class::cast)
            .map(ObservationTimeRegexParser::getYearMonth)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("ObservationTimeRegexParser not found in entries"));
  }

}
