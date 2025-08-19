package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.Wind;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WindRoseInputPort implements WindRoseUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public Map<Month, WindRose> generateMonthlyWindRose(String icao, RetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(icao, period);

		List<Metar> metarWithFixedDirectionWind = metarList.stream()
			                   .filter(m -> !m.getWind().getDirection().isVariable())
			                   .toList();

		return metarWithFixedDirectionWind.stream().collect(Collectors.groupingBy(m ->
			Month.from(m.getReportTime()),
				Collectors.collectingAndThen(
					Collectors.toList(),
					monthlyMetarList -> buildWindRoseForMonth(monthlyMetarList, speedBins, directionBins)
			)));
	}

	private WindRose buildWindRoseForMonth(List<Metar> metarList, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		Map<WindRose.BinPair, Long> freq = WindRose.initFrequencyMap(speedBins, directionBins);

		for (Metar metar : metarList) {
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
				.ifPresent(bin -> freq.put(bin, freq.get(bin)+1));
		}

		return new WindRose(speedBins, directionBins, freq, metarList.size());
	}

}
