package spec;

import exception.GenericSpecificationExeception;
import spec.shared.AbstractSpecification;

public class RunwayHeadingSpec extends AbstractSpecification<Integer> {

  @Override
  public boolean isSatisfiedBy(Integer heading) {
    return heading >= 0 && heading <= 36;
  }

  @Override
  public void check(Integer heading) throws GenericSpecificationExeception {
    if (!isSatisfiedBy(heading)) {
      throw new GenericSpecificationExeception("Heading must be between 0 and 36.");
    }
  }
}
