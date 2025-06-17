package model;

import lombok.Value;
import vo.unit.Unit;

public record ThresholdCondition(
    MetarMetricField  field,
    Comparison        comparison,
    double            threshold,
    Unit              unit
) {}
