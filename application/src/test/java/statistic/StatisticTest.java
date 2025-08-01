package statistic;

import dto.MetarRetrievalPeriod;
import dto.statistic.ObservationStatisticResponse;
import dto.statistic.ThresholdStatisticQuery;
import dto.statistic.WeatherStatisticQuery;
import model.generic.Comparison;
import model.weather.MetricField;
import model.weather.ThresholdCondition;
import model.weather.WeatherCondition;
import model.weather.WeatherConditionPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import port.input.CloudStatisticInputPort;
import port.input.ThresholdStatisticInputPort;
import port.input.WeatherStatisticInputPort;
import statistic.data.StatisticTestData;
import vo.unit.LengthUnit;

import java.util.List;

import static model.weather.MetricField.VISIBILITY;
import static model.weather.MetricField.WIND_SPEED;
import static model.weather.WeatherConditionPredicate.HAS_PHENOMENA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static vo.unit.LengthUnit.METERS;
import static vo.unit.SpeedUnit.KT;
import static vo.weather.type.WeatherPhenomenon.BR;

public class StatisticTest extends StatisticTestData {

	@BeforeEach
	void init() {
		thresholdStatisticUseCase = new ThresholdStatisticInputPort(metarManagementOutputPort);
		weatherStatisticUseCase = new WeatherStatisticInputPort(metarManagementOutputPort);
		cloudStatisticUseCase = new CloudStatisticInputPort(metarManagementOutputPort);
	}

	@Test
	void 시정통계_생성에_성공해야한다() {
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			"RKSI",
			new MetarRetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new ThresholdCondition(VISIBILITY, Comparison.LTE, 4000, METERS)
		);

		ObservationStatisticResponse actual = thresholdStatisticUseCase.execute(query);
		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(1, actual.hourly().size())
		);
	}

	@Test
	void 풍속통계_생성에_성공해야한다() {
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			"RKSI",
			new MetarRetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new ThresholdCondition(WIND_SPEED, Comparison.GTE, 5, KT)
		);

		ObservationStatisticResponse actual = thresholdStatisticUseCase.execute(query);
		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(1, actual.hourly().size())
		);
	}

	@Test
	void 날씨통계_생성에_성공해야한다() {
		WeatherStatisticQuery query = new WeatherStatisticQuery(
			"RKSI",
			new MetarRetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new WeatherCondition(HAS_PHENOMENA, List.of(BR))
		);

		ObservationStatisticResponse actual = weatherStatisticUseCase.execute(query);
		System.out.println(actual.toString());
		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(3, actual.hourly().size())
		);
	}



}
