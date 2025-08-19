package com.ilway.skystat.domain.spec;

import com.ilway.skystat.domain.exception.GenericSpecificationExeception;
import com.ilway.skystat.domain.spec.shared.AbstractSpecification;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeZoneSpec extends AbstractSpecification<ZonedDateTime> {

	@Override
	public boolean isSatisfiedBy(ZonedDateTime zonedDateTime) {
		return zonedDateTime.getZone().equals(ZoneOffset.UTC);
	}

	@Override
	public void check(ZonedDateTime zonedDateTime) throws GenericSpecificationExeception {
		if (!isSatisfiedBy(zonedDateTime)) {
			throw new GenericSpecificationExeception("TimeZone must be UTC, but: " + zonedDateTime.getZone());
		}
	}

}
