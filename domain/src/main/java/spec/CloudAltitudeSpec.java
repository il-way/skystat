package spec;

import exception.GenericSpecificationExeception;
import spec.shared.AbstractSpecification;
import vo.weather.Cloud;

public class CloudAltitudeSpec extends AbstractSpecification<Cloud> {

  @Override
  public boolean isSatisfiedBy(Cloud cloud) {
    return cloud.getAltitudeOptional()
            .map(altitude -> altitude < 100_000)
            .orElse(true);
  }

  @Override
  public void check(Cloud t) throws GenericSpecificationExeception {
    if (!isSatisfiedBy(t)) {
      throw new GenericSpecificationExeception("Cloud altitude can't be graeater than 100,000ft.");
    }
  }

}