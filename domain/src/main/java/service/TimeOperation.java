package service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeOperation {

	public static ZonedDateTime ofLenientUtc(int year, int month, int day, int hour, int minute) {
		int daysToAdd = hour / 24;
		int normalizedHour = hour % 24;
		return ZonedDateTime
			       .of(year, month, day, normalizedHour, minute, 0, 0, ZoneOffset.UTC)
			       .plusDays(daysToAdd);
	}

	public static ZonedDateTime toReportTime(ZonedDateTime observationTime) {
		ZonedDateTime utc = observationTime.withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime baseHour = utc.truncatedTo(ChronoUnit.HOURS);

		int minute = utc.getMinute();
		int second = utc.getSecond();
		int totalSeconds = minute * 60 + second;

		if (totalSeconds < 15 * 60) return baseHour;
		else if (totalSeconds < 45 * 60) return baseHour.plusMinutes(30);
		else return baseHour.plusHours(1);
	}

}
