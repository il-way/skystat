package com.ilway.skystat.domain.service;

import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.*;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.CB;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.NONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudOperationTest {

	@Test
	@DisplayName("최저 운저 검색에 성공해야 한다.")
	void getCeilingTest() {
		Clouds clouds = Clouds.builder()
			               .cloud(Cloud.of(FEW, 500, NONE))
			               .cloud(Cloud.of(BKN, 1500, CB))
			               .cloud(Cloud.of(VV, 200, NONE))
			               .cloud(Cloud.of(OVC, 10000, NONE))
			               .build();

		int ceiling = CloudOperation.getLowestCeiling(clouds);
		assertEquals(200, ceiling);
	}
}
