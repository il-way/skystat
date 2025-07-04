package condition;

import dto.taf.CloudConditionQuery;
import dto.taf.WeatherConditionQuery;
import model.weather.CloudCondition;
import model.weather.CloudConditionPredicate;
import static org.junit.jupiter.api.Assertions.*;

import model.weather.WeatherCondition;
import model.weather.WeatherConditionPredicate;
import org.junit.jupiter.api.Test;

import port.input.CloudConditionInputPort;
import port.input.WeatherConditionInputPort;
import usecase.ConditionUseCase;
import vo.weather.type.CloudType;
import vo.weather.type.WeatherPhenomenon;

import java.util.List;

public class ConditionTest extends TestData {

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

}
