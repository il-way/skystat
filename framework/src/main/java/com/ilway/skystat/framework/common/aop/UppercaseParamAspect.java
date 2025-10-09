package com.ilway.skystat.framework.common.aop;

import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;


@Aspect
@Component
public class UppercaseParamAspect {

	@Around("execution(* com.ilway.skystat.framework.adapter..*(..))")
	public Object uppercaseParam(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sig = (MethodSignature) pjp.getSignature();
		Method method = sig.getMethod();
		Object[] args = pjp.getArgs();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		for (int i=0; i<args.length; i++) {
			if (!hasUppercaseParam(parameterAnnotations[i])) continue;
			Object arg = args[i];
			if (arg == null) continue;

			if (arg instanceof String s) {
				args[i] = s.toUpperCase(Locale.ROOT);
			}
		}

		return pjp.proceed(args);
	}

	private boolean hasUppercaseParam(Annotation[] annotations) {
		for (Annotation anno : annotations) {
			if (anno.annotationType() == UppercaseParam.class) return true;
		}
		return false;
	}
}
