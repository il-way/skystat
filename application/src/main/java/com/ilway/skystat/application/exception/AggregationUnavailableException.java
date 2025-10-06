package com.ilway.skystat.application.exception;

public class AggregationUnavailableException extends BusinessException {

	public AggregationUnavailableException(String message, Throwable cause) {
		super(503, "AGGREGATION_UNAVAILABLE", message, cause);
	}

	public AggregationUnavailableException(String message) {
		this(message, null);
	}

}
