package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;

import java.util.List;

public interface WindRoseUseCase {

	WindRoseResult generateMonthlyWindRose(
		String icao,
		RetrievalPeriod period,
		List<SpeedBin> speedBins,
		List<DirectionBin> directionBins
	);

}
