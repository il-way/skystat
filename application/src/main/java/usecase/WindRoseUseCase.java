package usecase;

import dto.RetrievalPeriod;
import dto.windrose.DirectionBin;
import dto.windrose.SpeedBin;
import dto.windrose.WindRose;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface WindRoseUseCase {

	Map<Month, WindRose> generateMonthlyWindRose(
		String                icao,
		RetrievalPeriod period,
		List<SpeedBin>        speedBins,
		List<DirectionBin>    directionBins
	);

}
