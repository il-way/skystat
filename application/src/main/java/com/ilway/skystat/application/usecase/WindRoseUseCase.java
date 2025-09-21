package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface WindRoseUseCase {

	Map<Month, WindRose> generateMonthlyWindRose(
		String icao,
		RetrievalPeriod period,
		List<SpeedBin> speedBins,
		List<DirectionBin> directionBins
	);

}
