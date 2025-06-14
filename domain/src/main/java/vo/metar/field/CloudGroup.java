package vo.metar.field;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import lombok.*;
import vo.metar.type.CloudCoverage;

@Value
@Builder
public class CloudGroup {

  private final List<Cloud> clouds;

  public CloudGroup(List<Cloud> clouds) {
    this.clouds = List.copyOf(clouds);
  }

  public static CloudGroup of(List<Cloud> clouds) {
    return CloudGroup.builder()
        .clouds(clouds)
        .build();
  }

  public static CloudGroup ofEmpty() {
    return CloudGroup.builder()
        .clouds(List.of())
        .build();
  }

  public int size() {
    return clouds.size();
  }

  public int getLowestCeiling() {
    return getLowestCeiling(List.of(CloudCoverage.BKN, CloudCoverage.OVC, CloudCoverage.VV))
        .orElse(Integer.MAX_VALUE);
  }

  public OptionalInt getLowestCeiling(List<CloudCoverage> coverages) {
    return clouds.stream()
        .filter(cloud -> coverages.contains(cloud.getCoverage()))
        .map(Cloud::getAltitudeOptional)
        .flatMap(Optional::stream)
        .mapToInt(Integer::intValue)
        .min();
  }

}