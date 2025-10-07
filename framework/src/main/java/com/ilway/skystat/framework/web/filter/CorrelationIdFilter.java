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

	public static final String HEADER = "X-Correlation-Id";
	public static final String CORR_ID = "corrId";

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		String corr = Optional.ofNullable(req.getHeader(HEADER))
			              .filter(s -> !s.isBlank())
			              .orElse(UUID.randomUUID().toString());

		MDC.put(CORR_ID, corr);
		try {
			res.setHeader(HEADER, corr);
			filterChain.doFilter(req, res);
		} finally {
			MDC.clear();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		String uri = req.getRequestURI();
		return uri.startsWith("/actuator") || uri.startsWith("/health") || uri.startsWith("/static/");
	}
}
