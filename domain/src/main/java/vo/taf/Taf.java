package vo.taf;

import lombok.Builder;
import lombok.Value;
import vo.metar.ReportType;
import vo.taf.field.ForecastPeriod;
import vo.taf.field.TafSection;

import java.time.ZonedDateTime;
import java.util.List;

@Value
@Builder
public class Taf {

	private final String rawText;

	// Required
	private final String stationIcao;
	private final ReportType reportType;
	private final ZonedDateTime issuedTime;
	private final ForecastPeriod validPeriod;

	private final List<TafSection> sections;

	private final boolean isNill;
	private final boolean isCancelled;

	public Taf(String rawText, String stationIcao, ReportType reportType, ZonedDateTime issuedTime, ForecastPeriod validPeriod, List<TafSection> sections, boolean isNill, boolean isCancelled) {
		this.rawText = rawText;
		this.stationIcao = stationIcao;
		this.reportType = reportType;
		this.issuedTime = issuedTime;
		this.validPeriod = validPeriod;
		this.sections = sections;
		this.isNill = isNill;
		this.isCancelled = isCancelled;
	}
}
