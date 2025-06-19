package usecase;

import dto.MetarRetrievalPeriod;
import dto.windrose.DirectionBin;
import dto.windrose.SpeedBin;
import dto.windrose.WindRose;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface WindRoseUseCase {

	Map<Month, WindRose> generateMonthlyWindRose(
		String                icao,
		MetarRetrievalPeriod  period,
		List<SpeedBin>        speedBins,
		List<DirectionBin>    directionBins
	);

}
