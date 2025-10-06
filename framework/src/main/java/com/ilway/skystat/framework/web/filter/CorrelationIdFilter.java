package com.ilway.skystat.framework.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

	public static final String CORR_ID = "corrId";

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		String corr = Optional.ofNullable(req.getHeader("X-Correlation-Id"))
			              .filter(s -> !s.isBlank())
			              .orElse(UUID.randomUUID().toString());

		MDC.put(CORR_ID, corr);
		res.addHeader("X-Correlation-Id", corr);

		try {
			filterChain.doFilter(req, res);
		} finally {
			MDC.clear();
		}
	}
}
