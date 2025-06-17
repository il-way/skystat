package vo.metar.field;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import lombok.*;
import vo.metar.type.CloudCoverage;
import vo.metar.type.CloudType;
import vo.metar.type.Describable;

@Value
@Builder
public class CloudGroup {

  private final List<Cloud> cloudList;

  public CloudGroup(List<Cloud> cloudList) {
    this.cloudList = List.copyOf(cloudList);
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

  public boolean containsCloudType(List<Describable> targetList) {
    if (targetList == null) {
      throw new IllegalArgumentException("targetList mut not be null");
    }

    targetList.stream()
        .filter(t -> !(t instanceof CloudType))
        .findFirst()
        .ifPresent(t -> {
          throw new IllegalArgumentException(
              "All elements of targetList must be CloudType, but found: " +
                  targetList.getClass().getSimpleName());
        });

    if (targetList.isEmpty())
      return true;

    int targetIndex = 0;
    for (Cloud c : this.cloudList) {
      if (c.getType().equals(targetList.get(targetIndex))) {
        targetIndex++;
      }

      if (targetIndex == targetList.size()) {
        return true;
      }
    }

    return false;
  }

}