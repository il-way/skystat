package com.ilway.skystat.framework.exception;

import com.ilway.skystat.application.exception.BusinessException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MetarParseException extends BusinessException {

  public MetarParseException(String message, Throwable cause) {
    super(BAD_REQUEST.value(), "INVALID_METAR", message, cause);
  }

  public MetarParseException(String message) {
    this(message, null);
  }

}
