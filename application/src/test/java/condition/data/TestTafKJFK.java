package condition.data;

import lombok.Getter;
import vo.taf.Taf;
import vo.taf.field.ForecastPeriod;
import vo.taf.field.TafSection;
import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;
import vo.taf.type.ReportType;
import vo.weather.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static vo.unit.LengthUnit.MILE;
import static vo.unit.SpeedUnit.KT;
import static vo.weather.type.CloudCoverage.*;
import static vo.weather.type.CloudType.CB;
import static vo.weather.type.CloudType.NONE;
import static vo.weather.type.WeatherDescriptor.*;
import static vo.weather.type.WeatherInensity.LIGHT;
import static vo.weather.type.WeatherInensity.MODERATE;
import static vo.weather.type.WeatherPhenomenon.BR;
import static vo.weather.type.WeatherPhenomenon.RA;

@Getter
public class TestTafKJFK {

	/* ───── 원문 (2025-07-09) ───── */
	protected String rawText = """
		AMD KJFK 090843Z 0909/1012 24010KT P6SM BKN009 BKN250
		TEMPO 0910/0912 SCT009
		FM091400 24007KT P6SM FEW050 SCT250
		FM091800 19012KT P6SM SCT050 BKN250
		FM092100 19014KT P6SM BKN050 BKN200 PROB30 0922/1003 3SM TSRA BKN025CB
		FM100300 23007KT P6SM BKN040 BKN150 PROB30 1003/1006 3SM TSRA BKN025CB
		FM100600 22004KT P6SM VCRA BKN040 BKN100
		FM100900 VRB04KT 5SM -SHRA BR SCT025 OVC040
		""";

	protected String stationIcao = "KJFK";
	protected ReportType reportType = ReportType.AMD;
	protected ZonedDateTime issuedTime = ZonedDateTime.of(2025, 7, 9, 8, 43, 0, 0, ZoneOffset.UTC);
	protected ForecastPeriod validPeriod = ForecastPeriod.of(
		ZonedDateTime.of(2025, 7, 9, 9, 0, 0, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
	);

	protected TafSection headerSection = TafSection.of(
		Modifier.HEADER,
		validPeriod,
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(240), 10, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(WeatherGroup.of(List.of()))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 900, NONE),
				Cloud.of(BKN, 25000, NONE)
			)))
			.build()
	);

	protected TafSection section1 = TafSection.of(
		Modifier.TEMPO,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 9, 10, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 9, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(null)
			.visibility(null)
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
					Cloud.of(SCT, 900, NONE)
				))
			)
			.build()
	);

	protected TafSection section2 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 9, 14, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(240), 7, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(FEW, 5000, NONE),
				Cloud.of(SCT, 25000, NONE)
			)))
			.build()
	);

	protected TafSection section3 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 9, 18, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(190), 12, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(SCT, 5000, NONE),
				Cloud.of(BKN, 25000, NONE)
			)))
			.build()
	);

	protected TafSection section4 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 9, 21, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(190), 14, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 5000, NONE),
				Cloud.of(BKN, 20000, NONE)
			)))
			.build()
	);

	protected TafSection section5 = TafSection.of(
		Modifier.PROB30,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 9, 22, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 3, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, List.of(TS), List.of(RA))
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 2500, CB)
			)))
			.build()
	);

	protected TafSection section6 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 3, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(230), 7, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, NONE),
				Cloud.of(BKN, 15500, NONE)
			)))
			.build()
	);

	protected TafSection section7 = TafSection.of(
		Modifier.PROB30,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 3, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 6, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.visibility(Visibility.of(3, MILE))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, List.of(TS), List.of(RA))
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, CB),
				Cloud.of(BKN, 10000, CB)
			)))
			.build()
	);

	protected TafSection section8 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 6, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(220), 4, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, List.of(VC), List.of(RA))
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, CB),
				Cloud.of(BKN, 10000, CB)
			)))
			.build()
	);

	protected TafSection section9 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 9, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.variable(), 4, 0, KT))
			.visibility(Visibility.of(5, MILE))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(LIGHT, List.of(SH), List.of(RA)),
				Weather.of(MODERATE, List.of(), List.of(BR))
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 2500, CB)
			)))
			.build()
	);

	protected List<TafSection> sections = List.of(
		headerSection,
		section1,
		section2,
		section3,
		section4,
		section5,
		section6,
		section7,
		section8,
		section9
	);

	protected boolean isNill = false;
	protected boolean isCanceled = false;

	protected Taf taf = Taf.builder()
		                    .rawText(rawText)
		                    .stationIcao(stationIcao)
		                    .validPeriod(validPeriod)
		                    .reportType(reportType)
		                    .issuedTime(issuedTime)
		                    .sections(sections)
		                    .isNill(isNill)
		                    .isCanceled(isCanceled)
		                    .build();

}
