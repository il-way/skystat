package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record WindRoseResponse(
	ZonedDateTime coverageFrom,
	ZonedDateTime coverageTo,
	long totalCount,
	long missingCount,
	long sampleSize,
	List<String> speedBins,
	List<String> directionBins,
	List<Cell> data
	)  {

	record Cell(int month, int sbIndex, int dbIndex, long frequency, double rate) {}

	public static WindRoseResponse fromDefaultWindRose(PeriodInventory inventory, WindRoseResult result) {
		return from(inventory, SpeedBin.of5KtSpeedBins(), DirectionBin.of16DirectionBins(), result);
	}

	public static WindRoseResponse from(PeriodInventory inventory, List<SpeedBin> speedBins, List<DirectionBin> directionBins, WindRoseResult result) {
		List<String> sbLabels = labels(speedBins, SpeedBin::label);
		List<String> dbLabels = labels(directionBins, DirectionBin::label);
		List<Cell> cells = generateData(speedBins, directionBins, result);

		return new WindRoseResponse(
			inventory.firstAvailableTime(),
			inventory.lastAvailableTime(),
			result.totalCount(),
			result.missingCount(),
			result.sampleSize(),
			sbLabels,
			dbLabels,
			cells
		);
	}

	private static <T> List<String> labels(List<T> bins, Function<T, String> labeler) {
		return bins.stream().map(labeler).toList();
	}

	private static List<Cell> generateData(
		List<SpeedBin> speedBins, List<DirectionBin> directionBins, WindRoseResult result
	) {
		Map<Month, WindRose> map = result.windRoseMap();
		if (map == null || map.isEmpty()) return List.of();

		List<Cell> out = new ArrayList<>(speedBins.size() * directionBins.size() * map.size());

		// 인덱스 맵핑 (라벨 순서와 동일해야 함)
		Map<SpeedBin, Integer> sbIndex = new LinkedHashMap<>();
		for (int i = 0; i < speedBins.size(); i++) sbIndex.put(speedBins.get(i), i);

		Map<DirectionBin, Integer> dbIndex = new LinkedHashMap<>();
		for (int i = 0; i < directionBins.size(); i++) dbIndex.put(directionBins.get(i), i);

		map.forEach((month, windRose) -> {
			int m = month.getValue(); // 1~12
			for (SpeedBin sb : speedBins) {
				for (DirectionBin db : directionBins) {
					int si = sbIndex.get(sb);
					int di = dbIndex.get(db);
					long freq = windRose.getFrequency(sb, db);
					double rate = windRose.getRate(sb, db); // 필요시 소수점 자리수 반올림
					out.add(new Cell(m, si, di, freq, rate));
				}
			}
		});

		return out;
	}

}
