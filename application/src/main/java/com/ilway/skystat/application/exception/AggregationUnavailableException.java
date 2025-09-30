package com.ilway.skystat.application.exception;

public class AggregationUnavailableException extends RuntimeException {

	public AggregationUnavailableException(String msg) {
		super(msg);
	}

	public AggregationUnavailableException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
