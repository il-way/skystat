package model.weather;

import model.generic.Comparison;
import vo.unit.Unit;

public record ThresholdCondition(
	MetricField field,
	Comparison  comparison,
	double      threshold,
	Unit        unit
) {
}
