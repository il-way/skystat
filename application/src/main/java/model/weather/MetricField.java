package model.weather;

import lombok.RequiredArgsConstructor;
import vo.unit.LengthUnit;
import vo.unit.Unit;
import vo.weather.MetricSource;

import java.util.function.BiFunction;

import static vo.unit.LengthUnit.FEET;

@RequiredArgsConstructor
public enum MetricField {

  VISIBILITY((m,u) -> m.getVisibility().getUnit().convertTo(m.getVisibility().getValue(), u)),
  LOWEST_CEILING((m,u) -> FEET.convertTo(m.getCloudGroup().getLowestCeiling(), u)),
  PEAK_WIND((m,u) -> m.getWind().getUnit().convertTo(m.getWind().getPeakSpeed(), u)),
  WIND_SPEED((m,u) -> m.getWind().getUnit().convertTo(m.getWind().getSpeed(), u)),
  ALTIMETER((m,u) -> m.getAltimeter().getUnit().convertTo(m.getAltimeter().getValue(), u));

  private final BiFunction<MetricSource, Unit, Double> extractor;

  public double extract(MetricSource m, Unit inputUnit) {
    return extractor.apply(m, inputUnit);
  }

}
