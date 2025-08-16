package model.generic;

import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
public enum IntervalInclusion {

	OPEN((time, from, to) -> time.isAfter(from) && time.isBefore(to)),
	CLOSED((time, from, to) -> !time.isBefore(from) && !time.isAfter(to)),
	CLOSED_OPEN((time, from, to) -> !time.isBefore(from) && time.isBefore(to)),
	OPEN_CLOSED((time, from, to) -> time.isAfter(from) && !time.isAfter(to));

	private final TriPredicate<ZonedDateTime, ZonedDateTime, ZonedDateTime> op;

	public boolean test(ZonedDateTime time, ZonedDateTime from, ZonedDateTime to) {
		return op.test(time, from, to);
	}

}
