package taf;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import service.TafSnapshotExpander;
import taf.data.TestData;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import vo.unit.SpeedUnit;
import vo.weather.Visibility;
import vo.weather.WeatherGroup;
import vo.weather.Wind;
import vo.weather.WindDirection;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TafTest extends TestData {

	TafSnapshotExpander expander = new TafSnapshotExpander();

	@Test
	void BECMG은_이전예보를_대체한다() {
		Map<ZonedDateTime, WeatherSnapshot> expanded = expander.expand(tafMap.get("RKSS"));

		WeatherSnapshot becmg252000 = expanded.get(ofUTC(6, 25, 20, 0));
		WeatherSnapshot becmg252300 = expanded.get(ofUTC(6, 25, 23, 0));

		Visibility vis252000 = becmg252000.getVisibility();
		Wind wind252000 = becmg252000.getWind();
		WeatherGroup wg252000 = becmg252000.getWeatherGroup();

		Visibility vis252300 = becmg252300.getVisibility();
		Wind wind252300 = becmg252300.getWind();
		WeatherGroup wg252300 = becmg252300.getWeatherGroup();

		System.out.println(wg252000.toString());
		System.out.println(wg252300.toString());

		assertAll(
			() -> assertNotEquals(vis252000, vis252300),
			() -> assertNotEquals(wind252000, wind252300),
			() -> assertNotEquals(wg252000, wg252300)
		);
	}

	@Test
	void TEMPO는_동일_카테고리의_기상예보만을_대체한다() {
		Map<ZonedDateTime, WeatherSnapshot> expanded = expander.expand(tafMap.get("KJFK"));

		WeatherSnapshot fm092100 = expanded.get(ofUTC(7, 9, 21, 0));
		WeatherSnapshot pb092200 = expanded.get(ofUTC(7, 9, 22, 0));

		WeatherGroup wg092100 = fm092100.getWeatherGroup();
		Wind w092100 = fm092100.getWind();

		WeatherGroup wg092200 = pb092200.getWeatherGroup();
		Wind w092200 = pb092200.getWind();

		assertAll(
			() -> assertEquals(w092100, w092200),
			() -> assertNotEquals(wg092100, wg092200)
		);
	}

}

