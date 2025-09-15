package com.ilway.skystat.domain.vo.weather;

import lombok.EqualsAndHashCode;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import lombok.Getter;

import java.util.Optional;

import static com.ilway.skystat.domain.vo.weather.type.WindDirectionType.VARIABLE;

@EqualsAndHashCode
public class WindDirection {

  @Getter
  private final WindDirectionType type;

  private final Double degree;

  private WindDirection(WindDirectionType type, Double degree) {
    this.type = type;
    this.degree = degree;
  }

  public static WindDirection of(WindDirectionType type, Double degree) {
    if (type.equals(VARIABLE)) return variable();
    else return fixed(degree);
  }

  public static WindDirection fixed(double degree) {
    return new WindDirection(WindDirectionType.FIXED, degree);
  }

  public static WindDirection variable() {
    return new WindDirection(VARIABLE, null);
  }

  public double getDegreeOrThrow() {
    if (isVariable()) {
      throw new IllegalStateException("Variable wind has no fixed direction.");
    }
    return degree;
  }

  public Optional<Double> getDegreeOptional() {
    return Optional.ofNullable(degree);
  }

  public boolean isBetween(double lower, double upper) {
    double deg = getDegreeOrThrow();
    return deg <= upper && deg >= lower;
  }

  public boolean isVariable() {
    return type == VARIABLE;
  }

}
