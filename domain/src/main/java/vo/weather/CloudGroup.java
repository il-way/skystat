package vo.weather;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import lombok.*;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;

@Value
@Builder
public class CloudGroup {

  private final List<Cloud> cloudList;

  public CloudGroup(List<Cloud> cloudList) {
    this.cloudList = cloudList != null ? List.copyOf(cloudList) : List.of();
  }

  public static CloudGroup of(List<Cloud> cloudList) {
    return CloudGroup.builder()
        .cloudList(cloudList)
        .build();
  }

  public static CloudGroup ofEmpty() {
    return CloudGroup.builder()
        .cloudList(List.of())
        .build();
  }

  public int size() {
    return cloudList.size();
  }

  public int getLowestCeiling() {
    return getLowestCeiling(List.of(CloudCoverage.BKN, CloudCoverage.OVC, CloudCoverage.VV))
        .orElse(Integer.MAX_VALUE);
  }

  public OptionalInt getLowestCeiling(List<CloudCoverage> coverages) {
    return cloudList.stream()
        .filter(cloud -> coverages.contains(cloud.getCoverage()))
        .map(Cloud::getAltitudeOptional)
        .flatMap(Optional::stream)
        .mapToInt(Integer::intValue)
        .min();
  }

  public boolean containsCloudType(CloudType target) {
    return cloudList.stream()
        .anyMatch(cloud -> cloud.containCloudType(target));
  }

  public boolean containsCloudCoverage(CloudCoverage target) {
    return cloudList.stream()
        .anyMatch(cloud -> cloud.containCloudCoverage(target));
  }

}