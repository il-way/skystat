package com.ilway.skystat.application.dto.windrose;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WindRose {

	private final List<SpeedBin> speedBins;
	private final List<DirectionBin> directionBins;
	private final Map<BinPair, Long> frequencyMap;
	private final long totalCount;

	public record BinPair(SpeedBin speedBin, DirectionBin directionBin) {}

	@Builder
	public WindRose(List<SpeedBin> speedBins, List<DirectionBin> directionBins, Map<BinPair, Long> frequencyMap, long totalCount) {
		this.speedBins = List.copyOf(speedBins);
		this.directionBins = List.copyOf(directionBins);
		this.totalCount = totalCount;

		LinkedHashMap<BinPair, Long> map = initFrequencyMap(speedBins, directionBins);

		map.putAll(frequencyMap);
		this.frequencyMap = Collections.unmodifiableMap(map);
	}

	public long getFrequency(SpeedBin speedBin, DirectionBin directionBin) {
		return frequencyMap.getOrDefault(new BinPair(speedBin, directionBin), 0L);
	}

	public double getRate(SpeedBin speedBin, DirectionBin directionBin) {
		if (totalCount == 0) return 0;
		return (double) getFrequency(speedBin, directionBin) * 100 / totalCount;
	}

	public static LinkedHashMap<BinPair, Long> initFrequencyMap(List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		LinkedHashMap<BinPair, Long> map = new LinkedHashMap<>();
		for (SpeedBin sb : speedBins) {
			for (DirectionBin db : directionBins) {
				map.put(new BinPair(sb, db), 0L);
			}
		}
		return map;
	}

}
