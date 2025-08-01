package service;

import vo.weather.Cloud;
import vo.weather.CloudGroup;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class CloudOperation {

	public static int getLowestCeiling(CloudGroup cg) {
		return getLowestCeiling(cg, List.of(CloudCoverage.BKN, CloudCoverage.OVC, CloudCoverage.VV))
			       .orElse(Integer.MAX_VALUE);
	}

	public static OptionalInt getLowestCeiling(CloudGroup cg, List<CloudCoverage> coverages) {
		return cg.getCloudList().stream()
			       .filter(cloud -> coverages.contains(cloud.getCoverage()))
			       .map(Cloud::getAltitudeOptional)
			       .flatMap(Optional::stream)
			       .mapToInt(Integer::intValue)
			       .min();
	}

	public static boolean containsCloudType(CloudGroup cg, CloudType target) {
		return cg.getCloudList().stream()
			       .anyMatch(cloud -> cloud.containCloudType(target));
	}

	public static boolean containsCloudCoverage(CloudGroup cg, CloudCoverage target) {
		return cg.getCloudList().stream()
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
