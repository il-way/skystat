package com.ilway.skystat.framework.exception;

import com.ilway.skystat.application.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MetarParseException extends BusinessException {

  public MetarParseException(String message, Throwable cause) {
    super(HttpStatus.BAD_REQUEST.value(), "INVALID_METAR", message, cause);
  }

  public MetarParseException(String message) {
    this(message, null);
  }

}
