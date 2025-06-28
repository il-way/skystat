package taf;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import service.TafSnapshotExpander;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import vo.unit.SpeedUnit;
import vo.weather.Wind;
import vo.weather.WindDirection;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TafTest extends TafTestData {

	@Test
	void BECMG은_이전요소를_덮어쓴다() {
		TafTestData tafTestData = new TafTestData();
		Taf taf = tafTestData.generate();

		TafSnapshotExpander expander = new TafSnapshotExpander();
		Map<ZonedDateTime, WeatherSnapshot> expanded = expander.expand(taf);

		ZonedDateTime utc17 = generateUtc(6, 25, 17);
		ZonedDateTime utc22 = generateUtc(6, 25, 22);
		ZonedDateTime utc11 = generateUtc(6, 26, 11);

		WeatherSnapshot ws17 = expanded.get(utc17);

		Wind expected = Wind.of(WindDirection.fixed(200), 5, 0, SpeedUnit.KT);
		Wind actual = expanded.get(utc17).getWind();

		assertEquals(expected, actual);
	}


	private static ZonedDateTime generateUtc(int month, int day, int hour) {
		return ZonedDateTime.of(2025, month, day, hour, 0, 0, 0, ZoneOffset.UTC);
	}

}

