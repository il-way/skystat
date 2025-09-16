package com.ilway.skystat.framework.adapter.output.data.metar;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetarTestData {

	protected Map<String, List<Metar>> metarListMap = new HashMap<>();

	public MetarTestData() {
		init();
	}

	private void init() {
		List<Metar> listRksi = List.of(
			new RKSI202401010000().getTestData(),
			new RKSI202401010030().getTestData(),
			new RKSI202401010100().getTestData(),
			new RKSI202401010130().getTestData(),
			new RKSI202401010200().getTestData()
		);
		metarListMap.put("RKSI", listRksi);
	}

	protected ZonedDateTime ofUTC(int year, int month, int day, int hour, int min) {
		return ZonedDateTime.of(year, month, day, hour, min, 0, 0, ZoneOffset.UTC);
	}


}
