package windrose.data;

import dto.RetrievalPeriod;
import port.output.MetarManagementOutputPort;
import vo.metar.Metar;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WindRoseTestData {

	protected MetarManagementOutputPort metarManagementOutputPort;
	protected Map<String, List<Metar>> metarListMap = new HashMap<>();

	public WindRoseTestData() {
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

		metarManagementOutputPort = mock(MetarManagementOutputPort.class);
		when(metarManagementOutputPort.findByIcaoAndPeriod("RKSI",
			new RetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2025, 1, 1, 0, 0)
			)
		)).thenReturn(metarListMap.get("RKSI"));
	}

	protected ZonedDateTime ofUTC(int year, int month, int day, int hour, int min) {
		return ZonedDateTime.of(year, month, day, hour, min, 0, 0, ZoneOffset.UTC);
	}


}
