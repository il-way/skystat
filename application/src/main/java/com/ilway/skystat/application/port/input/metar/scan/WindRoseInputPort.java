package com.ilway.skystat.application.port.input.metar.scan;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.Wind;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public class WindRoseInputPort implements WindRoseUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public WindRoseResult generateDefault(String icao, RetrievalPeriod period) {
		return generate(icao, period, SpeedBin.of5KtSpeedBins(), DirectionBin.of16DirectionBins());
	}

	@Override
	public WindRoseResult generate(String icao, RetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		try {
			List<Metar> metars = metarManagementOutputPort.findByIcaoAndReportTimePeriod(icao, period);

			Map<Boolean, List<Metar>> partitionedByFixedDirectionExist = metars.stream()
				                                                             .collect(partitioningBy(m -> !m.getWind().getDirection().isVariable()));


			List<Metar> fixedDirections = partitionedByFixedDirectionExist.get(true);
			List<Metar> variableDirections = partitionedByFixedDirectionExist.get(false);

			Map<Month, WindRose> windrose = fixedDirections.stream()
				                                .collect(groupingBy(m -> Month.from(m.getReportTime()),
					                                collectingAndThen(
						                                toList(),
						                                monthlyMetars -> toWindRose(monthlyMetars, speedBins, directionBins))
				                                ));

			return new WindRoseResult(
				metars.size(),
				fixedDirections.size(),
				variableDirections.size(),
				windrose
			);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while generating wind rose", e);
		}
	}



	private WindRose toWindRose(List<Metar> metars, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		Map<WindRose.BinPair, Long> freq = WindRose.initFrequencyMap(speedBins, directionBins);

		for (Metar metar : metars) {
			Wind w = metar.getWind();
			double deg = w.getDirection().getDegreeOrThrow();

			speedBins.stream()
				.filter(sb -> w.isSpeedAtLeastAndLessThan(sb.lowerInclusive(), sb.upperExclusive(), sb.unit()))
				.findFirst()
				.flatMap(sb -> directionBins.stream()
					               .filter(db -> db.contains(deg))
					               .findFirst()
					               .map(db -> new WindRose.BinPair(sb, db))
				)
				.ifPresent(bin -> freq.put(bin, freq.get(bin) + 1));
		}

		return new WindRose(speedBins, directionBins, freq, metars.size());
	}

}
