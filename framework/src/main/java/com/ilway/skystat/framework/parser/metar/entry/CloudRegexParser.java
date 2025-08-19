package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.CloudRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.regex.Matcher;

public class CloudRegexParser extends ReportRegexParser<Cloud> {

  private static final MetarField FIELD_TYPE = MetarField.CLOUD;
  private static final String CLOUD_REGEX = CloudRegexes.fullPattern();

  @Override
  public Cloud parse(String rawText) {
    Matcher matcher = getMatcher(rawText, CLOUD_REGEX);

    if (!check(matcher)) {
      return null;
    }

    String coverageMatch = matcher.group(CloudRegexes.COVERAGE.getGroupName());
    String altitudeMatch = matcher.group(CloudRegexes.ALTITUDE.getGroupName());
    String typeMatch = matcher.group(CloudRegexes.TYPE.getGroupName());

    CloudCoverage coverage = CloudCoverage.valueOf(coverageMatch);
    CloudType type = typeMatch != null
            ? CloudType.valueOf(typeMatch)
            : CloudType.NONE;

    Integer altitude = altitudeMatch != null
            ? Integer.parseInt(altitudeMatch)*100
            : null;

    if (coverage.requiresAltitude()) {
      if (altitude == null) {
        throw new IllegalArgumentException("Altitude not found in report: " + rawText);
      }

      return Cloud.of(coverage, altitude, type);
    }

    return Cloud.of(coverage, altitude, type);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}