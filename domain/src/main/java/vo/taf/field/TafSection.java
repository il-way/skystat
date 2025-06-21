package vo.taf.field;

import lombok.Builder;
import lombok.Value;
import vo.taf.type.Modifier;

import java.time.ZonedDateTime;

@Value
@Builder
public class TafSection {

	private final Modifier modifier;
	private final ForecastPeriod period;
	private final WeatherSnapshot weather;

	public static TafSection of(Modifier modifier, ForecastPeriod period, WeatherSnapshot weather) {
		return TafSection.builder()
			       .modifier(modifier)
			       .period(period)
			       .weather(weather)
			       .build();
	}

	public boolean isApplicable(ZonedDateTime t) {
		return period.contains(t);
	}

}
