package com.ilway.skystat.framework.adapter.output.mysql.support;

import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.exception.ValidationException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Aspect
@Component
public class AdapterExceptionTranslationAspect {

	@Around("(within(com.ilway.skystat.framework.adapter.output.mysql..*)"
		        + " && (@within(com.ilway.skystat.framework.adapter.output.mysql.support.TranslateDbExceptions)"
		        + "  || @annotation(com.ilway.skystat.framework.adapter.output.mysql.support.TranslateDbExceptions)))")
	public Object translate(ProceedingJoinPoint pjp) throws Throwable {
		String op = resolveOp(pjp);
		try {
			return pjp.proceed();

		} catch (DataAccessException | PersistenceException e) {
			throw new AggregationUnavailableException("DB aggregation unavailable: " + op, e);

		} catch (IllegalArgumentException | ClassCastException e) {
			throw new ValidationException("INVALID_ARGUMENT", e.getMessage());

		} catch (BusinessException e) {
			throw e;

		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR.value(), "UNEXPECTED",
				"Unexpected error while " + op, e);
		}
	}

	private String resolveOp(ProceedingJoinPoint pjp) {
		var sig = (MethodSignature) pjp.getSignature();

		var mAnn = AnnotatedElementUtils.findMergedAnnotation(
			sig.getMethod(), TranslateDbExceptions.class);
		if (mAnn != null && !mAnn.value().isBlank()) return mAnn.value();

		var cAnn = AnnotatedElementUtils.findMergedAnnotation(
			sig.getDeclaringType(), TranslateDbExceptions.class);
		if (cAnn != null && !cAnn.value().isBlank()) return cAnn.value();

		return sig.toShortString();
	}

}
