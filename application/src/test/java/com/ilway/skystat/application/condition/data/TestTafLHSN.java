package com.ilway.skystat.application.condition.data;



import com.ilway.skystat.domain.vo.weather.*;
import lombok.Getter;
import com.ilway.skystat.domain.vo.taf.Taf;
import com.ilway.skystat.domain.vo.taf.field.ForecastPeriod;
import com.ilway.skystat.domain.vo.taf.field.TafSection;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.taf.type.Modifier;
import com.ilway.skystat.domain.vo.taf.type.ReportType;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.BKN;
import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.SCT;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.NONE;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.TCU;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.SH;
import static com.ilway.skystat.domain.vo.weather.type.WeatherInensity.LIGHT;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.RA;

@Getter
public class TestTafLHSN {

	/* ───── 원문 ───── */
	protected String rawText = """
      LHSN 101115Z 1012/1021 31012KT CAVOK
      PROB30 TEMPO 1012/1015 30016G28KT 9999 -SHRA SCT035TCU BKN050
      BECMG 1016/1019 30006KT
      """;

	/* ───── 메타데이터 ───── */
	protected String      stationIcao  = "LHSN";
	protected ReportType reportType   = ReportType.NORMAL;
	protected ZonedDateTime issuedTime = ZonedDateTime.of(
		2025, 7, 10, 11, 15, 0, 0, ZoneOffset.UTC);

	protected ForecastPeriod validPeriod = ForecastPeriod.of(
		ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2025, 7, 10, 21, 0, 0, 0, ZoneOffset.UTC));

	/* ───── HEADER 섹션 ───── */
	protected TafSection headerSection = TafSection.of(
		Modifier.HEADER,
		validPeriod,
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(310), 12, 0, KT))
			.visibility(Visibility.of(9999, METERS))       // CAVOK
			.weatherGroup(WeatherGroup.of(List.of()))
			.cloudGroup(CloudGroup.of(List.of()))          // CAVOK = NSC
			.build()
	);

	/* ───── PROB30 TEMPO 1012/1015 ───── */
	protected TafSection tempoProb30 = TafSection.of(
		Modifier.PROB30_TEMPO,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 15, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(300), 16, 28, KT))
			.visibility(Visibility.of(9999, METERS))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(LIGHT, List.of(SH), List.of(RA))   // -SHRA
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(SCT, 3500, TCU),
				Cloud.of(BKN, 5000, NONE)
			)))
			.build()
	);

	/* ───── BECMG 1016/1019 30006KT ───── */
	protected TafSection becmg1619 = TafSection.of(
		Modifier.BECMG,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 16, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 19, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(300), 6, 0, KT))
			.build()
	);

	/* ───── 섹션 모음 ───── */
	protected List<TafSection> sections = List.of(
		headerSection,
		tempoProb30,
		becmg1619
	);

	/* ───── 최종 TAF 객체 ───── */
	protected Taf taf = Taf.builder()
		                    .rawText(rawText)
		                    .stationIcao(stationIcao)
		                    .reportType(reportType)
		                    .issuedTime(issuedTime)
		                    .validPeriod(validPeriod)
		                    .sections(sections)
		                    .isNill(false)
		                    .isCanceled(false)
		                    .build();

}
