package com.ilway.skystat.application.port.input;

public interface ConditionUseCase<T> {

	boolean execute(T query);

}
