package com.ilway.skystat.framework.parser.metar.entry;

import com.ilway.skystat.framework.parser.metar.regex.CloudRegexes;
import com.ilway.skystat.framework.parser.shared.ReportRegexParser;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.CloudGroup;
import com.ilway.skystat.domain.vo.metar.MetarField;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class CloudGroupRegexParser extends ReportRegexParser<CloudGroup> {

  private static final MetarField FIELD_TYPE = MetarField.CLOUD_GROUP;
  private static final String CLOUD_REGEX = CloudRegexes.fullPattern();

  @Override
  public CloudGroup parse(String rawText) {
    Matcher matcher = getMatcher(rawText, CLOUD_REGEX);
    CloudRegexParser cloudParser = new CloudRegexParser();

    List<Cloud> clouds = new ArrayList<>();
    while (matcher.find()) {
      String matchedCloudText = matcher.group(0);
      try {
        Cloud cloud = cloudParser.parse(matchedCloudText);
        clouds.add(cloud);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage() + " (raw: " + rawText + ")");
      }
    }

    return CloudGroup.of(clouds);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}