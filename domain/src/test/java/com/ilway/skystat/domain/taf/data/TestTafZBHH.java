package com.ilway.skystat.domain.taf.data;


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
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.MPS;
import static com.ilway.skystat.domain.vo.weather.type.CloudCoverage.FEW;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.CB;
import static com.ilway.skystat.domain.vo.weather.type.CloudType.NONE;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.SH;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.TS;
import static com.ilway.skystat.domain.vo.weather.type.WeatherIntensity.LIGHT;
import static com.ilway.skystat.domain.vo.weather.type.WeatherIntensity.MODERATE;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.RA;

@Getter
public class TestTafZBHH {

	/* ───── 원문 ───── */
	protected String rawText = """
      AMD ZBHH 101222Z 1012/1112 10004MPS 9999 RA FEW040 TX28/1107Z TN18/1022Z
      TEMPO 1013/1014 TS FEW040CB FEW040
      TEMPO 1020/1102 2800 -SHRA FEW040CB FEW040
      """;

	/* ───── 메타데이터 ───── */
	protected String      stationIcao  = "ZBHH";
	protected ReportType  reportType   = ReportType.AMD;
	protected ZonedDateTime issuedTime = ZonedDateTime.of(
		2025, 7, 10, 12, 22, 0, 0, ZoneOffset.UTC);

	protected ForecastPeriod validPeriod = ForecastPeriod.of(
		ZonedDateTime.of(2025, 7, 10, 12, 0, 0, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2025, 7, 11, 12, 0, 0, 0, ZoneOffset.UTC));

	/* ───── HEADER 섹션 ───── */
	protected TafSection headerSection = TafSection.of(
		Modifier.HEADER,
		validPeriod,
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(100), 4, 0, MPS))   // 10004MPS
			.visibility(Visibility.of(9999, METERS))              // 9999 m
			.weathers(Weathers.of(List.of(
				Weather.of(MODERATE, List.of(), List.of(RA))
			)))             // CAVOK 아님->무현상
			.clouds(Clouds.of(List.of(                    // FEW040
				Cloud.of(FEW, 4000, NONE)
			)))
			.build()
	);

	/* ───── TEMPO 1012/1014 TS … ───── */
	protected TafSection tempo1214 = TafSection.of(
		Modifier.TEMPO,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 13, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 10, 14, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.weathers(Weathers.of(List.of(
				Weather.of(MODERATE, List.of(TS), List.of())          // TS (descriptor)
			)))
			.clouds(Clouds.of(List.of(
				Cloud.of(FEW, 4000, CB),
				Cloud.of(FEW, 4000, NONE)
			)))
			.build()
	);

	/* ───── TEMPO 1020/1102 2800 -SHRA … ───── */
	protected TafSection tempo201102 = TafSection.of(
		Modifier.TEMPO,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 10, 20, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 11, 2, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.visibility(Visibility.of(2800, METERS))
			.weathers(Weathers.of(List.of(
				Weather.of(LIGHT, List.of(SH), List.of(RA))       // -SHRA
			)))
			.clouds(Clouds.of(List.of(
				Cloud.of(FEW, 4000, CB),
				Cloud.of(FEW, 4000, NONE)
			)))
			.build()
	);

	/* ───── 섹션 리스트 ───── */
	protected List<TafSection> sections = List.of(
		headerSection,
		tempo1214,
		tempo201102
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
