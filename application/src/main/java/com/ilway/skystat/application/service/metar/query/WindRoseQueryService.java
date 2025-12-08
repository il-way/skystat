package com.ilway.skystat.application.service.metar.query;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.windrose.*;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.output.WindRoseQueryOutputPort;
import com.ilway.skystat.application.port.input.WindRoseUseCase;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public class WindRoseQueryService implements WindRoseUseCase {

	private final WindRoseQueryOutputPort port;

	@Override
	public WindRoseResult generateDefault(String icao, RetrievalPeriod period) {
		List<MonthlyWindRoseRow> rows = port.aggregateDefaultByMonth(icao, period);
		List<MonthlyCountDto> variables = port.countVariableByMonth(icao, period);

		int sampleSize = rows.stream().map(MonthlyWindRoseRow::freq)
			                 .mapToInt(Integer::intValue)
			                 .sum();

		int variableSize = variables.stream()
			                   .map(MonthlyCountDto::count)
			                   .mapToInt(Long::intValue)
			                   .sum();


		Map<Month, WindRose> windRoseMap = rows.stream().collect(groupingBy(
			r -> Month.of(r.month()),
			collectingAndThen(
				toList(),
				list -> toWindRose(list, SpeedBin.of5KtSpeedBins(), DirectionBin.of16DirectionBins())
			)
		));

		return new WindRoseResult(
			sampleSize + variableSize,
			sampleSize,
			variableSize,
			windRoseMap
		);
	}

	@Override
	public WindRoseResult generate(String icao, RetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		throw new BusinessException(501, "NOT_IMPLEMENTED", "Generate custom wind rose is not implemented yet.");
	}

	private WindRose toWindRose(List<MonthlyWindRoseRow> rows, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		LinkedHashMap<WindRose.BinPair, Long> freqMap = WindRose.initFrequencyMap(speedBins, directionBins);
		for (MonthlyWindRoseRow r : rows) {
			SpeedBin sb = speedBins.get(r.speedOrder());
			DirectionBin db = directionBins.get(r.dirOrder());
			WindRose.BinPair key = new WindRose.BinPair(sb, db);
			freqMap.put(key, freqMap.get(key) + r.freq());
		}

		long sampleSize = rows.stream().mapToLong(MonthlyWindRoseRow::freq).sum();
		return new WindRose(speedBins, directionBins, freqMap, sampleSize);
	}

}
