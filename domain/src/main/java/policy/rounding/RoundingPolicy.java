package policy.rounding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RoundingPolicy {
  
  private final int scale;
  private final RoundingMode roundingMode;
  
  private RoundingPolicy(int scale, RoundingMode roundingMode) {
    this.scale = scale;
    this.roundingMode = roundingMode;
  }

  public static RoundingPolicy of(int scale, RoundingMode roundingMode) {
    return new RoundingPolicy(scale, roundingMode);
  }

  public BigDecimal apply(BigDecimal value) {
    Objects.requireNonNull(value, "value must not be null.");
    return value.setScale(scale, roundingMode);
  }

  public double apply(double value) {
    BigDecimal rounded = apply(BigDecimal.valueOf(value));
    return rounded.doubleValue();
  }

  public long apply(long value) {
    BigDecimal rounded = apply(BigDecimal.valueOf(value));
    return rounded.longValue();
  }
  
}
