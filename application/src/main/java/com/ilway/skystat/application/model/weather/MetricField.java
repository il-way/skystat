package com.ilway.skystat.application.model.weather;

import lombok.RequiredArgsConstructor;
import com.ilway.skystat.domain.service.CloudOperation;
import com.ilway.skystat.domain.vo.unit.Unit;
import com.ilway.skystat.domain.vo.weather.MetricSource;

import java.util.function.BiFunction;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;

@RequiredArgsConstructor
public enum MetricField {

	ALTIMETER((m, u) -> m.getAltimeter().getUnit().convertTo(m.getAltimeter().getValue(), u)),
	LOWEST_CEILING((m, u) -> FEET.convertTo(CloudOperation.getLowestCeiling(m.getClouds()), u)),
	VISIBILITY((m, u) -> m.getVisibility().getUnit().convertTo(m.getVisibility().getValue(), u)),
//	TEMPERATURE((m, u) -> m.getTemperature().getUnit().convertTo(m.getTemperature().getValue(), u)),
	WIND_PEAK((m, u) -> m.getWind().getUnit().convertTo(m.getWind().getPeakSpeed(), u)),
	WIND_SPEED((m, u) -> m.getWind().getUnit().convertTo(m.getWind().getSpeed(), u));

	private final BiFunction<MetricSource, Unit, Double> extractor;

	public double extract(MetricSource m, Unit inputUnit) {
		return extractor.apply(m, inputUnit);
	}

}
