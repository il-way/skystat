package condition.data;

import dto.taf.CloudConditionQuery;
import dto.taf.WeatherConditionQuery;
import org.mockito.stubbing.OngoingStubbing;
import port.output.TafManagementOutputPort;
import usecase.ConditionUseCase;
import vo.taf.Taf;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConditionTestData {

	protected TafManagementOutputPort tafManagementOutputPort;
	protected ConditionUseCase<CloudConditionQuery> cloudConditionUseCase;
	protected ConditionUseCase<WeatherConditionQuery> weatherConditionUseCase;

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
		tafMap.put("rkss", rkss.taf);
		tafMap.put("kjfk", kjfk.taf);

		tafManagementOutputPort = mock(TafManagementOutputPort.class);
		when(tafManagementOutputPort.findByIcao("RKSS")).thenReturn(tafMap.get("rkss"));
		when(tafManagementOutputPort.findByIcao("KJFK")).thenReturn(tafMap.get("kjfk"));
	}

}
