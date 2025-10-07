package com.ilway.skystat.framework.observability.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(200)
public class LoggingAspect {

	@Value("${log.aop.slow-threshold-ms:1000}")
	private long slowMs;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	void controller() {}

	@Pointcut("this(com.ilway.skystat.application.usecase..*)")
	void usecase() {}

	@Around("controller() || usecase()")
	public Object roundtrip(ProceedingJoinPoint pjp) throws Throwable {
		String sig = pjp.getSignature().toShortString();
		long t0 = System.nanoTime();

		if (log.isDebugEnabled()) log.debug("→ {}", sig);
		try {
			Object ret = pjp.proceed();
			long ms = (System.nanoTime() - t0) / 1_000_000;
			if (ms >= slowMs) log.warn("← {} ok elapsedMs={}", sig, ms);
			else log.debug("← {} ok elapsedMs={}", sig, ms);
			return ret;

		} catch (Throwable e) {
			long ms = (System.nanoTime() - t0) / 1_000_000;
			log.debug("← {} fail elapsedMs={} msg={}", sig, ms, e.getMessage(), e);
			throw e;
		}
	}

}
