package vo.unit;

public interface Unit {

  double toBase(double value);
  double fromBase(double baseValue);

  default double convertTo(double value, Unit targetUnit) {
    double base = targetUnit.toBase(value);
    System.out.println("# convertTo toBase: " + base);
    double result = fromBase(base);
    System.out.println("# convertTo fromBase: " + result);
    return fromBase(base);
  }

}
