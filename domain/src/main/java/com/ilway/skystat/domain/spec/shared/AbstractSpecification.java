package com.ilway.skystat.domain.spec.shared;

import com.ilway.skystat.domain.exception.GenericSpecificationExeception;

public abstract class AbstractSpecification<T> implements Specification<T> {
  public abstract boolean isSatisfiedBy(T t);

  public abstract void check(T t) throws GenericSpecificationExeception;

  public Specification<T> and(Specification<T> other) {
    return new AndSpecification<T>(this, other);
  }
}
