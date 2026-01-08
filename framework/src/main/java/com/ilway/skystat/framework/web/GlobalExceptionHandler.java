package com.ilway.skystat.framework.web;

import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.exception.DuplicateMetarException;
import com.ilway.skystat.framework.exception.MetarParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
		logWarn(503, "SERVICE_UNAVAILABLE", e.getMessage(), req);
		return ResponseEntity.status(503).body(new ErrorBody("SERVICE_UNAVAILABLE", e.getMessage()));
	}

	@ExceptionHandler(DuplicateMetarException.class)
	public ResponseEntity<MetarSaveResponse> handleDuplicatedMetar(DuplicateMetarException e, HttpServletRequest req) {
		int status = e.getHttpStatus();
		String code = e.getCode();

		logWarn(status, code, e.getMessage(), req);
		return ResponseEntity.status(status).body(MetarSaveResponse.failure(0, 1, 0d, e.getMessage()));
	}

	@ExceptionHandler(MetarParseException.class)
	public ResponseEntity<MetarSaveResponse> handleMetarParse(MetarParseException e, HttpServletRequest req) {
		int status = e.getHttpStatus();
		String code = e.getCode();

		logWarn(status, code, e.getMessage(), req);
		return ResponseEntity.status(status).body(MetarSaveResponse.failure(1, 0, 1d, e.getMessage()));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorBody> handleBiz(BusinessException e, HttpServletRequest req) {
		int status = e.getHttpStatus();
		String code = e.getCode();

		if (status >= 500) {
			logError(status, code, e.getMessage(), req, e);
		} else {
			logWarn(status, code, e.getMessage(), req);
		}
		return ResponseEntity.status(status).body(new ErrorBody(code, e.getMessage()));
	}

	// === 400 계열 표준화 ===
	@ExceptionHandler({
		MethodArgumentTypeMismatchException.class,
		MissingServletRequestParameterException.class
	})
	public ResponseEntity<ErrorBody> handleBadRequest(Exception e, HttpServletRequest req) {
		logWarn(400, "BAD_REQUEST", e.getMessage(), req);
		return ResponseEntity.badRequest().body(new ErrorBody("BAD_REQUEST", e.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorBody> handleConstraint(ConstraintViolationException e, HttpServletRequest req) {
		String msg = e.getConstraintViolations().stream()
			             .map(v -> v.getPropertyPath() + " " + v.getMessage())
			             .findFirst().orElse(e.getMessage());
		logWarn(400, "VALIDATION_ERROR", msg, req);
		return ResponseEntity.badRequest().body(new ErrorBody("VALIDATION_ERROR", msg));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorBody> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest req) {
		String cause = (e.getMostSpecificCause() != null) ? e.getMostSpecificCause().getMessage() : e.getMessage();
		logWarn(400, "BAD_REQUEST", cause, req);
		return ResponseEntity.badRequest().body(new ErrorBody("BAD_REQUEST", "Malformed JSON or unreadable body"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorBody> handleMethodArgNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
		String msg = e.getBindingResult().getFieldErrors().stream()
			             .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
			             .findFirst().orElse("Validation failed");

		logWarn(400, "VALIDATION_ERROR", msg, req);
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
		logWarn(400, "BINDING_ERROR", msg, req);
		return ResponseEntity.badRequest().body(new ErrorBody("BINDING_ERROR", msg));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorBody> handleHandlerValidation(HandlerMethodValidationException e, HttpServletRequest req) {
		String msg = e.getAllErrors().stream()
			             .map(err -> err.getDefaultMessage())
			             .findFirst().orElse("Validation failed");
		logWarn(400, "VALIDATION_ERROR", msg, req);
		return ResponseEntity.badRequest().body(new ErrorBody("VALIDATION_ERROR", msg));
	}

	// 업로드 용량 초과 → 413
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorBody> handleUpload(MaxUploadSizeExceededException e, HttpServletRequest req) {
		logWarn(413, "PAYLOAD_TOO_LARGE", e.getMessage(), req);
		return ResponseEntity.status(413).body(new ErrorBody("PAYLOAD_TOO_LARGE", "File too large"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBody> handleAny(Exception e, HttpServletRequest req) {
		logError(500, "INTERNAL_ERROR", "Unexpected error", req, e);
		return ResponseEntity.internalServerError().body(new ErrorBody("INTERNAL_ERROR", "Unexpected error"));
	}

	// =========================================================
	// Logging helpers (slow/access와 동일한 키 순서/이름)
	// =========================================================

	private void logWarn(int status, String code, String msg, HttpServletRequest req) {
		log.warn("{} code={} msg={}",
			prefix(req, status),
			safe(code),
			safe(msg)
		);
	}

	private void logError(int status, String code, String msg, HttpServletRequest req, Throwable e) {
		log.error("{} code={} msg={}",
			prefix(req, status),
			safe(code),
			safe(msg),
			e
		);
	}

	private String prefix(HttpServletRequest req, int status) {
		return "corr=" + corrId(req)
			       + " ip=" + clientIp(req)
			       + " xff=\"" + safeHeader(req, "X-Forwarded-For") + "\""
			       + " method=" + safe(req.getMethod())
			       + " uri=\"" + safe(uri(req)) + "\""
			       + " status=" + status
			       + " referer=\"" + safeHeader(req, "Referer") + "\""
			       + " ua=\"" + safeHeader(req, "User-Agent") + "\"";
	}

	private String uri(HttpServletRequest req) {
		String qs = req.getQueryString();
		return (qs == null || qs.isBlank()) ? req.getRequestURI() : req.getRequestURI() + "?" + qs;
	}

	private String corrId(HttpServletRequest req) {
		Object v = req.getAttribute("corrId"); // log-spring-web 설정(request-attr)이 corrId인 경우
		if (v != null) return safe(String.valueOf(v));

		String mdc = MDC.get("corrId");
		return (mdc == null || mdc.isBlank()) ? "-" : mdc;
	}

	private String clientIp(HttpServletRequest req) {
		// 프록시 환경이면 XFF 첫 번째 값을 원본으로 간주(원하면 여기 정책 바꿀 수 있음)
		String xff = req.getHeader("X-Forwarded-For");
		if (xff != null && !xff.isBlank()) {
			int comma = xff.indexOf(',');
			return (comma >= 0) ? xff.substring(0, comma).trim() : xff.trim();
		}
		String ip = req.getRemoteAddr();
		return (ip == null || ip.isBlank()) ? "-" : ip;
	}

	private String safeHeader(HttpServletRequest req, String name) {
		String v = req.getHeader(name);
		if (v == null || v.isBlank()) return "-";
		return safe(v);
	}

	private String safe(String v) {
		if (v == null) return "-";
		String s = v.replace('\n', ' ')
			           .replace('\r', ' ')
			           .replace('\t', ' ')
			           .trim();
		if (s.isBlank()) return "-";
		if (s.length() > 500) return s.substring(0, 500) + "...(truncated)";
		return s;
	}

	public record ErrorBody(String code, String message) {}
}
