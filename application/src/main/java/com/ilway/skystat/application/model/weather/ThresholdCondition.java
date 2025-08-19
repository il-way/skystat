package com.ilway.skystat.application.model.weather;

import com.ilway.skystat.application.model.generic.Comparison;
import com.ilway.skystat.domain.vo.unit.Unit;

public record ThresholdCondition(
	MetricField field,
	Comparison  comparison,
	double      threshold,
	Unit        unit
) {
}
