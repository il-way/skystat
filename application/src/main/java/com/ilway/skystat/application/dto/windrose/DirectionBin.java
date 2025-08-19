package com.ilway.skystat.application.dto.windrose;

import java.util.List;
import java.util.stream.IntStream;

public record DirectionBin(double startDegInclusive, double endDegExclusive, String label) {

	public DirectionBin {
		if (startDegInclusive < 0 || endDegExclusive < 0
			    || startDegInclusive > 360 || endDegExclusive > 360) {
			throw new IllegalArgumentException("Direction degrees must be within [0, 360]: "
				                                   + "start=" + startDegInclusive
				                                   + ", end=" + endDegExclusive);
		}
		// end == start
		if (startDegInclusive == endDegExclusive) {
			throw new IllegalArgumentException("Direction bin must span a positive range (non-zero width): "
				                                   + "start=" + startDegInclusive
				                                   + ", end=" + endDegExclusive);
		}

		if (label==null || label.isBlank()) {
			throw new IllegalArgumentException("label can't be null and blank.");
		}
	}

	public boolean contains(double deg) {
		if (startDegInclusive <= endDegExclusive) {
			return deg >= startDegInclusive && deg < endDegExclusive;
		} else {
			return deg >= startDegInclusive || deg < endDegExclusive;
		}
	}

	public static List<DirectionBin> of16DirectionBins() {
		String[] labels = {
			"N", "NNE", "NE", "ENE",
			"E", "ESE", "SE", "SSE",
			"S", "SSW", "SW", "WSW",
			"W", "WNW", "NW", "NNW"
		};

		double binSize = 360.0 / labels.length; // 22.5도
		return IntStream.range(0, labels.length)
			       .mapToObj(i -> {
				       double center = i * binSize;
				       double start = (center - binSize/2 + 360) % 360;
				       double end = (center + binSize/2) % 360;
				       return new DirectionBin(start, end, labels[i]);
			       })
			       .toList();
	}

}
