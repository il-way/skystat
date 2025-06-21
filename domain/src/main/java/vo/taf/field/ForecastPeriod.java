package vo.taf.field;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import spec.TimeZoneSpec;

import java.time.ZonedDateTime;

@Value
@Builder
public class ForecastPeriod {

	private final ZonedDateTime from;
	private final ZonedDateTime to;

	private static final TimeZoneSpec timezoneSpec = new TimeZoneSpec();

	public static ForecastPeriod of(ZonedDateTime from, ZonedDateTime to) {
		timezoneSpec.check(from);
		timezoneSpec.check(to);

		return ForecastPeriod.builder()
			       .from(from)
			       .to(to)
			       .build();
	}

	public boolean contains(ZonedDateTime t) {
		return !t.isBefore(from) && !t.isBefore(to);
	}

}
