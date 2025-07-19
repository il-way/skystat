package service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeOperation {

	public static ZonedDateTime ofLenientUtc(int year, int month, int day, int hour, int minute) {
		int daysToAdd = hour / 24;
		int normalizedHour = hour % 24;
		return ZonedDateTime
			       .of(year, month, day, normalizedHour, minute, 0, 0, ZoneOffset.UTC)
			       .plusDays(daysToAdd);
	}

}
