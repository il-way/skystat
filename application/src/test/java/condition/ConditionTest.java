package condition;

import condition.data.ConditionTestData;
import condition.data.TestTafRKSS;
import dto.taf.CloudConditionQuery;
import dto.taf.WeatherConditionQuery;
import model.weather.CloudCondition;
import model.weather.CloudConditionPredicate;
import static org.junit.jupiter.api.Assertions.*;

import model.weather.WeatherCondition;
import model.weather.WeatherConditionPredicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import port.input.CloudConditionInputPort;
import port.input.WeatherConditionInputPort;
import service.TafSnapshotExpander;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
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
	}

	@Test
	void 구름조건_탐색에_성공해야한다() {
		cloudConditionUseCase = new CloudConditionInputPort(tafManagementOutputPort);
		CloudConditionQuery query = new CloudConditionQuery(
			"RKSS",
			ofUTC(6, 25, 17, 0),
			new CloudCondition(CloudConditionPredicate.HAS_CLOUDTYPE, CloudType.CB)
		);

		Boolean actual = cloudConditionUseCase.execute(query);
		assertEquals(false, actual);
	}

	@Test
	void 날씨조건_탐색에_성공해야한다() {
		weatherConditionUseCase = new WeatherConditionInputPort(tafManagementOutputPort);
		WeatherConditionQuery query = new WeatherConditionQuery(
			"RKSS",
			ofUTC(6, 25, 22, 0),
			new WeatherCondition(WeatherConditionPredicate.HAS_PHENOMENA, List.of(WeatherPhenomenon.BR))
		);

		Boolean actual = weatherConditionUseCase.execute(query);
		assertEquals(true, actual);
	}

	@Test
	void FM은_이전_예보를_완전히_대체한다() {
		WeatherConditionQuery query100600 = new WeatherConditionQuery(
			"KJFK",
			ofUTC(7, 10, 6, 0),
			new WeatherCondition(
				WeatherConditionPredicate.HAS_DESCRIPTORS_AND_PHENOMENA,
				List.of(WeatherDescriptor.VC, WeatherPhenomenon.RA))
		);

		WeatherConditionQuery query100900 = new WeatherConditionQuery(
			"KJFK",
			ofUTC(7, 10, 9, 0),
			new WeatherCondition(
				WeatherConditionPredicate.HAS_PHENOMENA,
				List.of(WeatherPhenomenon.RA))
		);

		WeatherConditionQuery query100900_2 = new WeatherConditionQuery(
			"KJFK",
			ofUTC(7, 10, 9, 0),
			new WeatherCondition(
				WeatherConditionPredicate.HAS_DESCRIPTORS,
				List.of(WeatherDescriptor.VC))
		);

		boolean fm100600 = weatherConditionUseCase.execute(query100600);
		boolean fm100900 = weatherConditionUseCase.execute(query100900);
		boolean fm100900_2 = weatherConditionUseCase.execute(query100900_2);

		assertAll(
			() -> assertTrue(fm100600),
			() -> assertTrue(fm100900),
			() -> assertFalse(fm100900_2)
		);
	}

}
