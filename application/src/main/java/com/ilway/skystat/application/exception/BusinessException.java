package com.ilway.skystat.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessException extends RuntimeException {

	private final int httpStatus;
	private final String code;

	public BusinessException(int httpStatus, String code, String message) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
	}

	public BusinessException(int httpStatus, String code, String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
		this.code = code;
	}

}
