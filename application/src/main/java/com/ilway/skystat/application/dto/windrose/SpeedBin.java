package com.ilway.skystat.application.dto.windrose;

import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import java.util.List;
import java.util.Objects;

public record SpeedBin(int lowerInclusive, int upperExclusive, SpeedUnit unit, String label) {

	public SpeedBin {
		if (lowerInclusive < 0 || upperExclusive < 0) {
			throw new IllegalArgumentException("Speed bounds must be non-negative : "
				                                   + "lowerInclusive=" + lowerInclusive
				                                   + ", upperExclusive=" + upperExclusive
			);
		}

		if (upperExclusive <= lowerInclusive) {
			throw new IllegalArgumentException("upperExclusive must be greater than lowerInclusive: "
				                                   + "lowerInclusive=" + lowerInclusive
				                                   + ", upperExclusive=" + upperExclusive);
		}

		Objects.requireNonNull(unit, "unit must not be null");

		if (label==null || label.isBlank()) {
			throw new IllegalArgumentException("label can't be null and blank.");
		}
	}

	public static List<SpeedBin> of5KtSpeedBins() {
		return List.of(
			new SpeedBin(0, 1, SpeedUnit.KT, "calm"),
			new SpeedBin(1, 5, SpeedUnit.KT, "1-5KT"),
			new SpeedBin(5, 10, SpeedUnit.KT, "5-10KT"),
			new SpeedBin(10, 15, SpeedUnit.KT, "10-15KT"),
			new SpeedBin(15, 20, SpeedUnit.KT, "15-20KT"),
			new SpeedBin(20, 25, SpeedUnit.KT, "20-25KT"),
			new SpeedBin(25, 9999, SpeedUnit.KT, "25+KT")
		);
	}

}