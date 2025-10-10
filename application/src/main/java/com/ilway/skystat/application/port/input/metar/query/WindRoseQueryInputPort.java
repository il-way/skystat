package com.ilway.skystat.application.port.input.metar.query;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.windrose.*;
import com.ilway.skystat.application.port.output.WindRoseQueryOutputPort;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public class WindRoseQueryInputPort implements WindRoseUseCase {

	private final WindRoseQueryOutputPort port;

	@Override
	public WindRoseResult generateDefaultMonthlyWindRose(String icao, RetrievalPeriod period) {
		return generateMonthlyWindRose(icao, period, SpeedBin.of5KtSpeedBins(), DirectionBin.of16DirectionBins());
	}

	@Override
	public WindRoseResult generateMonthlyWindRose(String icao, RetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		List<MonthlyWindRoseRow> rows = port.aggregateByMonth(icao, period);
		List<MonthlyCountDto> variables = port.countVariableByMonth(icao, period);

		Map<Month, Long> fixedByMonth = rows.stream().collect(groupingBy(
				r -> Month.of(r.month()), collectingAndThen(
					toList(),
					list -> list.isEmpty() ? 0L : list.getFirst().fixedSample())
			)
		);

		Map<Month, Long> varByMonth = variables.stream().collect(toMap(
			v -> Month.of(v.month()),
			v -> v.count(),
			Long::sum
		));

		Map<Month, WindRose> windRoseMap = rows.stream().collect(groupingBy(
			r -> Month.of(r.month()),
			collectingAndThen(
				toList(),
				list -> toWindRose(list, speedBins, directionBins)
			)
		));

		int fixedTotal = fixedByMonth.values().stream().mapToInt(Long::intValue).sum();
		int variableTotal = varByMonth.values().stream().mapToInt(Long::intValue).sum();

		return new WindRoseResult(
			fixedTotal + variableTotal,
			fixedTotal,
			variableTotal,
			windRoseMap
		);
	}

	private WindRose toWindRose(List<MonthlyWindRoseRow> rows, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		LinkedHashMap<WindRose.BinPair, Long> freqMap = WindRose.initFrequencyMap(speedBins, directionBins);
		for (MonthlyWindRoseRow r : rows) {
			SpeedBin sb = speedBins.get(r.speedOrder());
			DirectionBin db = directionBins.get(r.dirOrder());
			WindRose.BinPair key = new WindRose.BinPair(sb, db);
			freqMap.put(key, freqMap.get(key) + r.freq());
		}
		long sampleSize = rows.isEmpty() ? 0 : rows.getFirst().fixedSample();

		return new WindRose(speedBins, directionBins, freqMap, sampleSize);
	}

}
