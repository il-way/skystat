package com.ilway.skystat.domain.vo.weather;

import java.util.List;

import lombok.*;

@Value
@Builder
public class Clouds {

  @Singular("cloud")
  private final List<Cloud> clouds;

  public Clouds(List<Cloud> clouds) {
    this.clouds = clouds != null ? List.copyOf(clouds) : List.of();
  }

  public static Clouds of(List<Cloud> cloudList) {
    return Clouds.builder()
        .clouds(cloudList)
        .build();
  }

  public static Clouds ofEmpty() {
    return Clouds.builder()
        .clouds(List.of())
        .build();
  }

  public int size() {
    return clouds.size();
  }



}