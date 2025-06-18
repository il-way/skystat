package vo.metar.field;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import spec.CloudAltitudeSpec;
import spec.CloudCoverageSpec;
import vo.metar.type.CloudCoverage;
import vo.metar.type.CloudType;

import java.util.List;
import java.util.Optional;

@EqualsAndHashCode
public class Cloud {

  @Getter
  private final CloudCoverage coverage;

  private final Integer altitude;

  @Getter
  private final CloudType type;

  private static final CloudCoverageSpec coverageSpec = new CloudCoverageSpec();
  private static final CloudAltitudeSpec altitudeSpec = new CloudAltitudeSpec();

  @Builder
  public Cloud(CloudCoverage coverage, Integer altitude, CloudType type) {
    this.coverage = coverage;
    this.altitude = altitude;
    this.type = type;

    coverageSpec.check(this);
    altitudeSpec.check(this);
  }

  public static Cloud of(CloudCoverage coverage, Integer altitude, CloudType type) {
    return Cloud.builder()
            .coverage(coverage)
            .altitude(altitude)
            .type(type)
            .build();
  }

  public boolean isAltitudeAtMost(int threshold, List<CloudCoverage> targetCoverages) {
    for (CloudCoverage target : targetCoverages) {
      if (!target.requiresAltitude()) {
        throw new IllegalArgumentException(target + " has no fixed altitude.");
      }
    }

    if (!targetCoverages.contains(coverage)) {
      return false;
    }

    return getAltitudeOptional()
            .map((altitude) -> altitude <= threshold)
            .orElse(false);
  }

  public int getAltitudeOrThrow() {
    return getAltitudeOptional()
            .orElseThrow(() -> new IllegalStateException(coverage + " has no fixed altitude."));
  }

  public Optional<Integer> getAltitudeOptional() {
    return Optional.ofNullable(altitude);
  }

  public boolean hasCloudType() {
    return type != CloudType.NONE;
  }

  public boolean containCloudType(CloudType target) {
    return type == target;
  }

  public boolean containCloudCoverage(CloudCoverage target) {
    return coverage == target;
  }

}
