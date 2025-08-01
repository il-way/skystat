package statistic.data;

import dto.MetarRetrievalPeriod;
import dto.statistic.CloudStatisticQuery;
import dto.statistic.ThresholdStatisticQuery;
import dto.statistic.WeatherStatisticQuery;
import port.output.MetarManagementOutputPort;
import port.output.TafManagementOutputPort;
import service.TimeOperation;
import usecase.StatisticUseCase;
import vo.metar.Metar;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticTestData {

	protected MetarManagementOutputPort metarManagementOutputPort;
	protected StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase;
	protected StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase;
	protected StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase;

	protected Map<String, List<Metar>> metarListMap = new HashMap<>();

	public StatisticTestData() {
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
			new MetarRetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			)
		)).thenReturn(metarListMap.get("RKSI"));
	}

	protected ZonedDateTime ofUTC(int year, int month, int day, int hour, int min) {
		return ZonedDateTime.of(2025, month, day, hour, min, 0, 0, ZoneOffset.UTC);
	}

}
