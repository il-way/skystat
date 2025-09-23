package com.ilway.skystat.domain.metar;

import org.junit.jupiter.api.Test;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;

import java.util.List;

public class CloudsTest {

  @Test
  void 구름군에서_특정_구름타입의_존재여부_검증에_성공해야한다() {
    Cloud cloud = Cloud.of(CloudCoverage.BKN, 300, CloudType.CB);
    Clouds clouds = Clouds.of(List.of(cloud));

    boolean expected = true;



  }

}
