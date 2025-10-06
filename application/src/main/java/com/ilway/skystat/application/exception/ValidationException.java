package com.ilway.skystat.application.exception;

public class ValidationException extends BusinessException {

	public ValidationException(String code, String message) {
		super(400, code, message);
	}

}
