package com.ilway.skystat.domain.vo.weather;

import java.util.List;

import lombok.*;

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



}