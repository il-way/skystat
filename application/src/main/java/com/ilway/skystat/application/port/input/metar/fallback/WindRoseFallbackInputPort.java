package com.ilway.skystat.application.port.input.metar.fallback;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WindRoseFallbackInputPort implements WindRoseUseCase {

	private final WindRoseUseCase dbUseCase;
	private final WindRoseUseCase scanUseCase;

	@Override
	public WindRoseResult generateDefault(String icao, RetrievalPeriod period) {
		try {
			return dbUseCase.generateDefault(icao, period);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.generateDefault(icao, period);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while generating wind rose", e);
		}
	}

	@Override
	public WindRoseResult generate(String icao, RetrievalPeriod period, List<SpeedBin> speedBins, List<DirectionBin> directionBins) {
		try {
			return dbUseCase.generate(icao, period, speedBins, directionBins);
		} catch (AggregationUnavailableException e) {
			return scanUseCase.generate(icao, period, speedBins, directionBins);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(500, "UNEXPECTED", "Unexpected error while generating wind rose", e);
		}
	}

}
