package port.input;

import dto.MetarRetrievalPeriod;
import dto.windrose.DirectionBin;
import dto.windrose.SpeedBin;
import dto.windrose.WindRose;
import lombok.RequiredArgsConstructor;
import port.output.MetarManagementOutputPort;
import usecase.WindRoseUseCase;
import vo.metar.Metar;
import vo.weather.Wind;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WindRoseInputPort implements WindRoseUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public Map<Month, WindRose> generateMonthlyWindRose(String icao, MetarRetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
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
