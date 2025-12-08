package com.ilway.skystat.application.condition.data;

import com.ilway.skystat.application.dto.taf.CloudConditionQuery;
import com.ilway.skystat.application.dto.taf.ThresholdConditionQuery;
import com.ilway.skystat.application.dto.taf.WeatherConditionQuery;
import com.ilway.skystat.application.port.output.TafManagementOutputPort;
import com.ilway.skystat.application.port.input.ConditionUseCase;
import com.ilway.skystat.domain.vo.taf.Taf;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConditionTestData {

	protected TafManagementOutputPort tafManagementOutputPort;
	protected ConditionUseCase<CloudConditionQuery> cloudConditionUseCase;
	protected ConditionUseCase<WeatherConditionQuery> weatherConditionUseCase;
	protected ConditionUseCase<ThresholdConditionQuery> thresholdConditionUseCase;

	protected Map<String, Taf> tafMap = new HashMap<>();

	public ConditionTestData() {
		init();
	}

	protected ZonedDateTime ofUTC(int month, int day, int hour, int min) {
		return ZonedDateTime.of(2025, month, day, hour, min, 0, 0, ZoneOffset.UTC);
	}

	private void init() {
		TestTafRKSS rkss = new TestTafRKSS();
		TestTafKJFK kjfk = new TestTafKJFK();
		TestTafLHSN lhsn = new TestTafLHSN();
		TestTafZBHH zbhh = new TestTafZBHH();
		TestTafKORF korf = new TestTafKORF();

		tafMap.put("RKSS", rkss.getTaf());
		tafMap.put("KJFK", kjfk.getTaf());
		tafMap.put("LHSN", lhsn.getTaf());
		tafMap.put("ZBHH", zbhh.getTaf());
		tafMap.put("KORF", korf.getTaf());

		tafManagementOutputPort = mock(TafManagementOutputPort.class);
		when(tafManagementOutputPort.findByIcao("RKSS")).thenReturn(tafMap.get("RKSS"));
		when(tafManagementOutputPort.findByIcao("KJFK")).thenReturn(tafMap.get("KJFK"));
		when(tafManagementOutputPort.findByIcao("LHSN")).thenReturn(tafMap.get("LHSN"));
		when(tafManagementOutputPort.findByIcao("ZBHH")).thenReturn(tafMap.get("ZBHH"));
		when(tafManagementOutputPort.findByIcao("KORF")).thenReturn(tafMap.get("KORF"));
	}

}
