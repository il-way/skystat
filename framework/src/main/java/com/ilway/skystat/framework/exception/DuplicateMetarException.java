package com.ilway.skystat.framework.exception;

import com.ilway.skystat.application.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.*;

public class DuplicateMetarException extends BusinessException {

	public DuplicateMetarException(String message, Throwable cause) {
		super(CONFLICT.value(), "DUPLICATED_METAR", message, cause);
	}

	public DuplicateMetarException(String message) {
		this(message, null);
	}

	public DuplicateMetarException(String icao, ZonedDateTime observationTime, String rawText) {
		super(CONFLICT.value(), "DUPLICATED_METAR", "Duplicated METAR: %s @ %s".formatted(icao, observationTime, rawText), null);
	}

}
