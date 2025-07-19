package condition.data;

import lombok.Getter;
import service.TimeOperation;
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

import static service.TimeOperation.ofLenientUtc;
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
public class TestTafKORF {

	/* ───── 원문 (2025-07-19) ───── */
	protected String rawText = """
      AMD KORF 190241Z 1903/1924 10007KT P6SM VCTS BKN040CB
      TEMPO 1903/1905 VRB15G30KT 3SM -SHRA BKN040
      FM190500 00000KT P6SM VCSH BKN045
      FM191100 VRB05KT P6SM BKN030
      """;

	/* ───── 메타데이터 ───── */
	protected String      stationIcao  = "KORF";
	protected ReportType  reportType   = ReportType.AMD;
	protected ZonedDateTime issuedTime = ZonedDateTime.of(
		2025, 7, 19, 2, 41, 0, 0, ZoneOffset.UTC);

	protected ForecastPeriod validPeriod = ForecastPeriod.of(
		ofLenientUtc(2025, 7, 19, 3, 0),
		ofLenientUtc(2025, 7, 19, 24, 0)
	);

	/* ───── HEADER 10007KT P6SM VCTS BKN040CB ───── */
	protected TafSection header = TafSection.of(
		Modifier.HEADER,
		validPeriod,
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(100), 7, 0, KT))
			.visibility(Visibility.of(6, MILE))                    // P6SM
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, List.of(VC, TS), List.of())   // VCTS
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, CB)
			)))
			.build()
	);

	/* ───── TEMPO 1903/1905 VRB15G30KT 3SM -SHRA BKN040 ───── */
	protected TafSection tempo0305 = TafSection.of(
		Modifier.TEMPO,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 19, 3, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 7, 19, 5, 0, 0, 0, ZoneOffset.UTC)
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.variable(), 15, 30, KT))
			.visibility(Visibility.of(3, MILE))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(LIGHT, List.of(SH), List.of(RA))        // -SHRA
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4000, NONE)
			)))
			.build()
	);

	/* ───── FM190500 00000KT P6SM VCSH BKN045 ───── */
	protected TafSection fm0500 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 19, 5, 0, 0, 0, ZoneOffset.UTC),
			validPeriod.getTo()
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.fixed(0), 0, 0, KT))       // Calm
			.visibility(Visibility.of(6, MILE))
			.weatherGroup(WeatherGroup.of(List.of(
				Weather.of(MODERATE, List.of(VC, SH), List.of())   // VCSH
			)))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 4500, NONE)
			)))
			.build()
	);

	/* ───── FM191100 VRB05KT P6SM BKN030 ───── */
	protected TafSection fm1100 = TafSection.of(
		Modifier.FM,
		ForecastPeriod.of(
			ZonedDateTime.of(2025, 7, 19, 11, 0, 0, 0, ZoneOffset.UTC),
			validPeriod.getTo()
		),
		WeatherSnapshot.builder()
			.wind(Wind.of(WindDirection.variable(), 5, 0, KT))
			.visibility(Visibility.of(6, MILE))
			.cloudGroup(CloudGroup.of(List.of(
				Cloud.of(BKN, 3000, NONE)
			)))
			.build()
	);

	/* ───── 섹션 리스트 ───── */
	protected List<TafSection> sections = List.of(
		header,
		tempo0305,
		fm0500,
		fm1100
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
