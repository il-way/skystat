package taf.data;

import lombok.Getter;
import vo.taf.Taf;
import vo.taf.field.ForecastPeriod;
import vo.taf.field.TafSection;
import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;
import vo.taf.type.ReportType;
import vo.weather.*;
import vo.weather.type.WeatherInensity;
import vo.weather.type.WeatherPhenomenon;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static vo.unit.LengthUnit.METERS;
import static vo.unit.SpeedUnit.KT;
import static vo.weather.type.CloudCoverage.*;
import static vo.weather.type.CloudType.NONE;

@Getter
public class TestTafRKSS {

	protected String rawText = """
		RKSS 251100Z 2512/2618 03006KT 6000 FEW010 BKN027 BKN070 TN20/2520Z TX27/2605Z
		BECMG 2515/2517 20005KT
		BECMG 2518/2520 17005KT 4000 BR
		BECMG 2521/2523 26007KT 9999 NSW BKN040
		BECMG 2609/2611 SCT040
		BECMG 2613/2615 20005KT 6000 FEW010 BKN030
		""";

	protected String stationIcao = "RKSS";
	protected ReportType reportType = ReportType.NORMAL;
	protected ZonedDateTime issuedTime = ZonedDateTime.of(2025, 6, 25, 11, 0, 0, 0, ZoneOffset.UTC);
	protected ForecastPeriod validPeriod = ForecastPeriod.of(
		ZonedDateTime.of(2025, 6, 25, 12, 0, 0, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2025, 6, 26, 18, 0, 0, 0, ZoneOffset.UTC)
	);

	protected TafSection headerSection = TafSection.of(
		Modifier.HEADER,
		validPeriod,
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(30), 6, 0, KT))
			.visibility(Visibility.of(6000, METERS))
			.weatherGroup(WeatherGroup.of(List.of()))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(FEW, 1000, NONE),
				Cloud.of(BKN, 2700, NONE),
				Cloud.of(BKN, 7000, NONE)
			)))
			.build()
	);

	protected TafSection section1 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 25, 15, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 25, 17, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(200), 5, 0, KT))
			.visibility(null)
			.weatherGroup(null)
			.cloudGroup(null)
			.build()
	);

	protected TafSection section2 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 25, 18, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 25, 20, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(170), 5, 0, KT))
			.visibility(Visibility.of(4000, METERS))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(WeatherInensity.MODERATE, null, List.of(WeatherPhenomenon.BR))
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, NONE)
			)))
			.build()
	);

	protected TafSection section3 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 25, 21, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 25, 23, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(260), 7, 0, KT))
			.visibility(Visibility.of(9999, METERS))
			.weatherGroup(WeatherGroup.of(
					Weather.of(WeatherInensity.MODERATE, null, List.of(WeatherPhenomenon.NSW))
				)
			)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, NONE)
			)))
			.build()
	);

	protected TafSection section4 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 26, 9, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 26, 11, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(null)
			.visibility(null)
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(SCT, 4000, NONE)
			)))
			.build()
	);

	protected TafSection section5 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 26, 13, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 26, 15, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(200), 5, 0, KT))
			.visibility(Visibility.of(6000, METERS))
			.weatherGroup(null)
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(FEW, 1000, NONE),
				Cloud.of(BKN, 3000, NONE)
			)))
			.build()
	);

	protected List<TafSection> sections = List.of(
		headerSection,
		section1,
		section2,
		section3,
		section4,
		section5
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
