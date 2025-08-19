package com.ilway.skystat.application.usecase;

public interface ConditionUseCase<T> {

	boolean execute(T query);

}
