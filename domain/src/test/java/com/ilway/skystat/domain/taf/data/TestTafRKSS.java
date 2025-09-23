package com.ilway.skystat.domain.taf.data;

import com.ilway.skystat.domain.vo.weather.*;
import lombok.Getter;
import com.ilway.skystat.domain.vo.taf.Taf;
import com.ilway.skystat.domain.vo.taf.field.ForecastPeriod;
import com.ilway.skystat.domain.vo.taf.field.TafSection;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.taf.type.Modifier;
import com.ilway.skystat.domain.vo.taf.type.ReportType;
import com.ilway.skystat.domain.vo.weather.type.WeatherInensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.*;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.*;
import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.*;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.*;

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
			.weathers(Weathers.of(List.of()))
			.clouds(Clouds.of(List.of(
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
			.weathers(null)
			.clouds(null)
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
			.weathers(Weathers.of(List.of(
				Weather.of(WeatherInensity.MODERATE, null, List.of(WeatherPhenomenon.BR))
			)))
			.clouds(Clouds.of(List.of(
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
			.weathers(Weathers.of(
					Weather.of(WeatherInensity.MODERATE, null, List.of(WeatherPhenomenon.NSW))
				)
			)
			.clouds(Clouds.of(List.of(
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
			.weathers(null)
			.clouds(Clouds.of(List.of(
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
			.weathers(null)
			.clouds(Clouds.of(List.of(
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
