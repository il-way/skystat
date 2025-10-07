package com.ilway.skystat.framework.web;

import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AggregationUnavailableException.class)
	public ResponseEntity<ErrorBody> handleUnavailable(AggregationUnavailableException e, HttpServletRequest req) {
		log.warn("503 path={} msg={}", req.getRequestURI(), e.getMessage());
		return ResponseEntity.status(503).body(new ErrorBody("SERVICE_UNAVAILABLE", e.getMessage()));
	}

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

	// === 400 계열 표준화 ===
	@ExceptionHandler({
		MethodArgumentTypeMismatchException.class,
		MissingServletRequestParameterException.class
	})
	public ResponseEntity<ErrorBody> handleBadRequest(Exception e, HttpServletRequest req) {
		log.warn("400 path={} msg={}", req.getRequestURI(), e.getMessage());
		return ResponseEntity.badRequest().body(new ErrorBody("BAD_REQUEST", e.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorBody> handleConstraint(ConstraintViolationException e, HttpServletRequest req) {
		String msg = e.getConstraintViolations().stream()
			             .map(v -> v.getPropertyPath() + " " + v.getMessage())
			             .findFirst().orElse(e.getMessage());
		log.warn("400 path={} msg={}", req.getRequestURI(), msg);
		return ResponseEntity.badRequest().body(new ErrorBody("VALIDATION_ERROR", msg));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorBody> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest req) {
		log.warn("400 path={} msg={}", req.getRequestURI(), e.getMostSpecificCause().getMessage());
		return ResponseEntity.badRequest().body(new ErrorBody("BAD_REQUEST", "Malformed JSON or unreadable body"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorBody> handleMethodArgNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
		String msg = e.getBindingResult().getFieldErrors().stream()
			             .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
			             .findFirst().orElse("Validation failed");
		log.warn("400 path={} msg={}", req.getRequestURI(), msg);
		return ResponseEntity.badRequest().body(new ErrorBody("VALIDATION_ERROR", msg));
	}

	@ExceptionHandler({ BindException.class, ConversionFailedException.class })
	public ResponseEntity<ErrorBody> handleBind(Exception e, HttpServletRequest req) {
		String msg;
		if (e instanceof BindException be) {
			msg = be.getBindingResult().getFieldErrors().stream()
				      .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
				      .findFirst().orElse("Binding failed");
		} else {
			msg = e.getMessage();
		}
		log.warn("400 path={} msg={}", req.getRequestURI(), msg);
		return ResponseEntity.badRequest().body(new ErrorBody("BINDING_ERROR", msg));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorBody> handleHandlerValidation(HandlerMethodValidationException e, HttpServletRequest req) {
		String msg = e.getAllErrors().stream()
			             .map(err -> err.getDefaultMessage())
			             .findFirst().orElse("Validation failed");
		log.warn("400 path={} msg={}", req.getRequestURI(), msg);
		return ResponseEntity.badRequest().body(new ErrorBody("VALIDATION_ERROR", msg));
	}

	// (선택) 업로드 용량 초과 → 413
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorBody> handleUpload(MaxUploadSizeExceededException e, HttpServletRequest req) {
		log.warn("413 path={} msg={}", req.getRequestURI(), e.getMessage());
		return ResponseEntity.status(413).body(new ErrorBody("PAYLOAD_TOO_LARGE", "File too large"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBody> handleAny(Exception e, HttpServletRequest req) {
		log.error("unhandled error path={}", req.getRequestURI(), e);
		return ResponseEntity.internalServerError().body(new ErrorBody("INTERNAL_ERROR","Unexpected error"));
	}

	public record ErrorBody(String code, String message) {}

}
