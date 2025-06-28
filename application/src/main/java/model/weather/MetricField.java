package model.weather;

import lombok.RequiredArgsConstructor;
import vo.unit.LengthUnit;
import vo.unit.Unit;
import vo.weather.MetricSource;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public enum MetricField {

  VISIBILITY((m,u) -> u.convertTo(m.getVisibility().getValue(), m.getVisibility().getUnit())),
  LOWEST_CEILING((m,u) -> u.convertTo(m.getCloudGroup().getLowestCeiling(), LengthUnit.FEET)),
  PEAK_WIND((m,u) -> u.convertTo(m.getWind().getPeakSpeed(), m.getWind().getUnit())),
  WIND_SPEED((m,u) -> u.convertTo(m.getWind().getSpeed(), m.getWind().getUnit())),
  ALTIMETER((m,u) -> u.convertTo(m.getAltimeter().getValue(), m.getAltimeter().getUnit()));

  private final BiFunction<MetricSource, Unit, Double> extractor;

  public double extract(MetricSource m, Unit inputUnit) {
    return extractor.apply(m, inputUnit);
  }

}
