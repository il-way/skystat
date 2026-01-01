package com.ilway.skystat.framework.adapter.output.mysql.aop;

import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.exception.ValidationException;
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Aspect
@Component
public class AdapterExceptionTranslationAspect {

	@Around("(within(com.ilway.skystat.framework.adapter.output.mysql.statistic..*)")
	public Object translateStatisticQuery(ProceedingJoinPoint pjp) throws Throwable {
		if (!hasTranslateAnnotation(pjp)) {
			return pjp.proceed();
		}
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

	@Around("(within(com.ilway.skystat.framework.adapter.output.mysql.management..*)")
	public Object translateManagementQuery(ProceedingJoinPoint pjp) throws Throwable {
		if (!hasTranslateAnnotation(pjp)) {
			return pjp.proceed();
		}
		String op = resolveOp(pjp);
		try {
			return pjp.proceed();

		} catch (DataAccessException | PersistenceException  | BusinessException e) {
			throw e;

		} catch (IllegalArgumentException | ClassCastException e) {
			throw new ValidationException("INVALID_ARGUMENT", e.getMessage());

		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR.value(), "UNEXPECTED",
				"Unexpected error while " + op, e);
		}
	}

	private boolean hasTranslateAnnotation(ProceedingJoinPoint pjp) {
		MethodSignature sig = (MethodSignature) pjp.getSignature();
		Class<?> targetClass = AopUtils.getTargetClass(pjp.getTarget());

		// 메서드 애노테이션 (구현 메서드)
		var specificMethod = AopUtils.getMostSpecificMethod(sig.getMethod(), targetClass);
		var mAnn = AnnotatedElementUtils.findMergedAnnotation(specificMethod, TranslateDbExceptions.class);
		if (mAnn != null) return true;

		// 클래스 애노테이션 (구현 클래스)
		var cAnn = AnnotatedElementUtils.findMergedAnnotation(targetClass, TranslateDbExceptions.class);
		return cAnn != null;
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
