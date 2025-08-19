package com.ilway.skystat.domain.vo.weather;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.ilway.skystat.domain.spec.CloudAltitudeSpec;
import com.ilway.skystat.domain.spec.CloudCoverageSpec;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;

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
