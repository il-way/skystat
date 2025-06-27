package taf;

import org.junit.jupiter.api.Test;
import service.TafSnapshotExpander;
import vo.taf.Taf;
import vo.taf.field.ForecastPeriod;
import vo.taf.field.TafSection;
import vo.taf.field.WeatherSnapshot;
import vo.taf.type.Modifier;
import vo.taf.type.ReportType;
import static vo.unit.LengthUnit.*;
import static vo.unit.SpeedUnit.*;
import vo.weather.*;
import static vo.weather.type.CloudCoverage.*;
import static vo.weather.type.CloudType.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class TafTest {

	@Test
	void print() {
		String rawText = """
			RKSS 251100Z 2512/2618 03006KT 6000 FEW010 BKN027 BKN070 TN20/2520Z TX27/2605Z
			BECMG 2515/2517 20005KT
			BECMG 2518/2520 17005KT 4000 BR
			BECMG 2521/2523 26007KT 9999 NSW BKN040
			BECMG 2609/2611 SCT040
			BECMG 2613/2615 20005KT 6000 FEW010 BKN030
			""";

		String stationIcao = "RKSS";
		ReportType reportType = ReportType.NORMAL;
		ZonedDateTime issuedTime = ZonedDateTime.of(2025, 6, 25, 11, 0, 0, 0, ZoneOffset.UTC);

		ForecastPeriod headerPeriod = ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 25, 12, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 26, 18, 0, 0, 0, ZoneOffset.UTC)
		);

		WeatherSnapshot headerSnapshot = WeatherSnapshot.builder()
      .wind(Wind.of(WindDirection.fixed(30), 6, 0, KT))
      .visibility(Visibility.of(6000, METERS))
      .weatherGroup(WeatherGroup.of(List.of()))
      .cloudGroup(CloudGroup.of(List.of(
        Cloud.of(FEW, 1000, NONE),
        Cloud.of(BKN, 2700, NONE),
        Cloud.of(BKN, 7000, NONE)
      )))
      .build();

		TafSection tafHeaderSection = TafSection.of(Modifier.HEADER, headerPeriod, headerSnapshot);


		ForecastPeriod secondLinePeriod = ForecastPeriod.of(
			ZonedDateTime.of(2025, 6, 25, 15, 0, 0, 0, ZoneOffset.UTC),
			ZonedDateTime.of(2025, 6, 25, 17, 0, 0, 0, ZoneOffset.UTC)
		);

		WeatherSnapshot secondLineSnapshot = WeatherSnapshot.builder()
			                                 .wind(Wind.of(WindDirection.fixed(200), 5, 0, KT))
			                                 .visibility(null)
			                                 .weatherGroup(null)
			                                 .cloudGroup(null)
			                                 .build();

		TafSection tafSecondSection = TafSection.of(Modifier.BECMG, secondLinePeriod, secondLineSnapshot);
		Taf taf = Taf.builder()
			            .rawText(rawText)
			            .stationIcao(stationIcao)
			            .reportType(reportType)
			            .validPeriod(headerPeriod)
			            .issuedTime(issuedTime)
			            .sections(List.of(
				            tafHeaderSection,
				            tafSecondSection
			            ))
			            .isCanceled(false)
			            .isNill(false)
			            .build();

		TafSnapshotExpander expander = new TafSnapshotExpander();
		List<WeatherSnapshot> expanded = expander.expand(taf);
		System.out.println(expanded);

	}

}
