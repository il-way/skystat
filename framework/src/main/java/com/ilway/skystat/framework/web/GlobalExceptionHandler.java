package com.ilway.skystat.framework.web;

import com.ilway.skystat.application.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class) // 필요 시 도메인/앱 예외로 교체
	public ResponseEntity<ErrorBody> handleBiz(BusinessException e, HttpServletRequest req) {
		int status = e.getHttpStatus();
		String code = e.getCode();

		if (status >= 500) {
			log.error("{} path={} code={} msg={}", status, req.getRequestURI(), code, e.getMessage(), e);
		} else {
			log.warn("{} path={} code={} msg={}", status, req.getRequestURI(), code, e.getMessage());
		}
		return ResponseEntity.status(status).body(new ErrorBody(code, e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBody> handleAny(Exception e, HttpServletRequest req) {
		log.error("unhandled error path={}", req.getRequestURI(), e);
		return ResponseEntity.internalServerError().body(new ErrorBody("INTERNAL_ERROR","Unexpected error"));
	}

	public record ErrorBody(String code, String message) {}

}
