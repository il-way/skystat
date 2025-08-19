package com.ilway.skystat.domain.taf;

import static org.junit.jupiter.api.Assertions.*;

import com.ilway.skystat.domain.service.WeatherOperation;
import org.junit.jupiter.api.Test;
import com.ilway.skystat.domain.service.TafSnapshotExpander;
import com.ilway.skystat.domain.taf.data.TestData;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;
import com.ilway.skystat.domain.vo.weather.Visibility;
import com.ilway.skystat.domain.vo.weather.WeatherGroup;
import com.ilway.skystat.domain.vo.weather.Wind;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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

		assertAll(
			() -> assertNotEquals(vis252000, vis252300),
			() -> assertNotEquals(wind252000, wind252300),
			() -> assertNotEquals(wg252000, wg252300)
		);
	}

	@Test
	void TEMPO는_동일그룹의_일기현상이_아니면_기존_현상목록에_추가한다() {
		Map<ZonedDateTime, WeatherSnapshot> expanded = expander.expand(tafMap.get("ZBHH"));

		WeatherSnapshot header101200 = expanded.get(ofUTC(7, 10, 12, 0));
		WeatherSnapshot tempo101300 = expanded.get(ofUTC(7, 10, 13, 0));

		WeatherGroup wg101200 = header101200.getWeatherGroup();
		WeatherGroup wg101300 = tempo101300.getWeatherGroup();

		System.out.println(wg101200.toString());
		System.out.println(wg101300.toString());

		assertAll(
			() -> assertTrue(WeatherOperation.containsPhenomena(wg101200, List.of(WeatherPhenomenon.RA))),
			() -> assertTrue(WeatherOperation.containsPhenomena(wg101300, List.of(WeatherPhenomenon.RA))),
			() -> assertFalse(WeatherOperation.containsDescriptors(wg101200, List.of(WeatherDescriptor.TS))),
			() -> assertTrue(WeatherOperation.containsDescriptors(wg101300, List.of(WeatherDescriptor.TS)))
		);
	}

	@Test
	void FM은_이전_예보를_완전히_대체한다() {

	}

}

