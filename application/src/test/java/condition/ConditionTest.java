package condition;

import condition.data.ConditionTestData;
import condition.data.TestTafRKSS;
import dto.taf.CloudConditionQuery;
import dto.taf.ThresholdConditionQuery;
import dto.taf.WeatherConditionQuery;
import model.generic.Comparison;
import model.weather.*;

import static org.junit.jupiter.api.Assertions.*;
import static vo.unit.LengthUnit.FEET;
import static vo.unit.LengthUnit.METERS;
import static vo.unit.SpeedUnit.KT;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import port.input.CloudConditionInputPort;
import port.input.ThresholdConditionInputPort;
import port.input.WeatherConditionInputPort;
import service.TafSnapshotExpander;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import vo.unit.LengthUnit;
import vo.weather.WeatherGroup;
import vo.weather.Wind;
import vo.weather.type.CloudType;
import vo.weather.type.WeatherDescriptor;
import vo.weather.type.WeatherPhenomenon;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ConditionTest extends ConditionTestData {

	@BeforeEach
	void init() {
		cloudConditionUseCase = new CloudConditionInputPort(tafManagementOutputPort);
		weatherConditionUseCase = new WeatherConditionInputPort(tafManagementOutputPort);
		thresholdConditionUseCase = new ThresholdConditionInputPort(tafManagementOutputPort);
	}

	@Test
	void 구름조건_탐색에_성공해야한다() {
		CloudConditionQuery query = new CloudConditionQuery(
			"RKSS",
			ofUTC(6, 25, 17, 0),
			new CloudCondition(CloudConditionPredicate.HAS_CLOUDTYPE, CloudType.CB)
		);

		boolean actual = cloudConditionUseCase.execute(query);
		assertFalse(actual);
	}

	@Test
	void 날씨조건_탐색에_성공해야한다() {
		WeatherConditionQuery query = new WeatherConditionQuery(
			"RKSS",
			ofUTC(6, 25, 22, 0),
			new WeatherCondition(WeatherConditionPredicate.HAS_PHENOMENA, List.of(WeatherPhenomenon.BR))
		);

		boolean actual = weatherConditionUseCase.execute(query);
		assertTrue(actual);
	}

	@Test
	void 바람_임계조건_탐색에_성공해야한다() {
		ThresholdConditionQuery query = new ThresholdConditionQuery(
			"ZBHH",
			ofUTC(7, 10, 13, 0),
			new ThresholdCondition(MetricField.WIND_SPEED, Comparison.GTE, 5, KT)
		);

		boolean actual = thresholdConditionUseCase.execute(query);
		assertTrue(actual);
	}

	@Test
	void 최대풍속_임계조건_탐색에_성공해야한다() {
		ThresholdConditionQuery query = new ThresholdConditionQuery(
			"KORF",
			ofUTC(7, 19, 4, 0),
			new ThresholdCondition(MetricField.PEAK_WIND, Comparison.GTE, 25, KT)
		);

		boolean actual = thresholdConditionUseCase.execute(query);
		assertTrue(actual);
	}

	@Test
	void 시정_임계조건_탐색에_성공해야한다() {
		ThresholdConditionQuery query = new ThresholdConditionQuery(
			"KORF",
			ofUTC(7, 19, 4, 0),
			new ThresholdCondition(MetricField.VISIBILITY, Comparison.LTE, 6000, METERS)
		);

		boolean actual = thresholdConditionUseCase.execute(query);
		assertTrue(actual);
	}

	@Test
	void 운저_임계조건_탐색에_성공해야한다() {
		ThresholdConditionQuery query = new ThresholdConditionQuery(
			"KJFK",
			ofUTC(7, 9, 21, 0),
			new ThresholdCondition(MetricField.LOWEST_CEILING, Comparison.LTE, 6000, FEET)
		);

		boolean actual = thresholdConditionUseCase.execute(query);
		assertTrue(actual);
	}

}
