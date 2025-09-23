package com.ilway.skystat.domain.service;

import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class CloudOperation {

	public static int getLowestCeiling(Clouds cg) {
		return getLowestCeiling(cg, List.of(CloudCoverage.BKN, CloudCoverage.OVC, CloudCoverage.VV))
			       .orElse(Integer.MAX_VALUE);
	}

	public static OptionalInt getLowestCeiling(Clouds cg, List<CloudCoverage> coverages) {
		return cg.getClouds().stream()
			       .filter(cloud -> coverages.contains(cloud.getCoverage()))
			       .map(Cloud::getAltitudeOptional)
			       .flatMap(Optional::stream)
			       .mapToInt(Integer::intValue)
			       .min();
	}

	public static boolean containsCloudType(Clouds cg, CloudType target) {
		return cg.getClouds().stream()
			       .anyMatch(cloud -> cloud.containCloudType(target));
	}

	public static boolean containsCloudCoverage(Clouds cg, CloudCoverage target) {
		return cg.getClouds().stream()
			       .anyMatch(cloud -> cloud.containCloudCoverage(target));
	}


	public static boolean isAltitudeAtMost(Cloud c, int threshold, List<CloudCoverage> targetCoverages) {
		for (CloudCoverage target : targetCoverages) {
			if (!target.requiresAltitude()) {
				throw new IllegalArgumentException(target + " has no fixed altitude.");
			}
		}

		if (!targetCoverages.contains(c.getCoverage())) {
			return false;
		}

		return c.getAltitudeOptional()
			       .map((altitude) -> altitude <= threshold)
			       .orElse(false);
	}

}
