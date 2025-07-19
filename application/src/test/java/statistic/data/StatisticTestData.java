package statistic.data;

import port.output.TafManagementOutputPort;
import vo.metar.Metar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticTestData {

	protected TafManagementOutputPort tafManagementOutputPort;

	protected Map<String, List<Metar>> metarListMap = new HashMap<>();

	public StatisticTestData() {
		init();
	}

	private void init() {
		metarListMap.put("RKSI", List.of());
	}

}
