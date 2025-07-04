package parser.metar.entry;

import parser.metar.regex.CloudRegexes;
import parser.shared.ReportRegexParser;
import vo.weather.Cloud;
import vo.weather.CloudGroup;
import vo.metar.MetarField;

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
      Cloud cloud = cloudParser.parse(matchedCloudText);
      clouds.add(cloud);
    }

    return CloudGroup.of(clouds);
  }

  @Override
  public MetarField getFieldType() {
    return FIELD_TYPE;
  }

}