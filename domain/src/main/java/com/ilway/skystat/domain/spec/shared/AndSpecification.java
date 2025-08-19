package com.ilway.skystat.domain.spec.shared;

import com.ilway.skystat.domain.exception.GenericSpecificationExeception;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AndSpecification<T> extends AbstractSpecification<T> {

  private final Specification<T> spec1;
  private final Specification<T> spec2;

  @Override
  public boolean isSatisfiedBy(T t) {
    return spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
  }

  @Override
  public void check(T t) throws GenericSpecificationExeception {

  }

}