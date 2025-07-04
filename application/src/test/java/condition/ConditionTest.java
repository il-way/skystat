package condition;

import dto.taf.CloudConditionQuery;
import dto.taf.WeatherConditionQuery;
import model.weather.CloudConditionPredicate;
import model.weather.CloudGroupField;
import static org.junit.jupiter.api.Assertions.*;

import model.weather.WeatherConditionPredicate;
import model.weather.WeatherGroupField;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import port.input.CloudConditionInputPort;
import port.input.WeatherConditionInputPort;
import port.output.TafManagementOutputPort;
import usecase.ConditionUseCase;
import vo.taf.Taf;
import vo.weather.type.CloudType;
import vo.weather.type.WeatherPhenomenon;

import java.time.ZonedDateTime;
import java.util.List;

public class ConditionTest extends TestData {

	@Test
	void 구름조건_탐색에_성공해야한다() {
		conditionUseCase = new CloudConditionInputPort(tafManagementOutputPort);
		CloudConditionQuery query = new CloudConditionQuery(
			"RKSS",
			ofUTC(6, 25, 17, 0),
			new CloudConditionPredicate(CloudGroupField.HAS_CLOUDTYPE, CloudType.CB)
		);

		Boolean actual = conditionUseCase.execute(query);
		assertEquals(actual, false);
	}

	@Test
	void 날씨조건_탐색에_성공해야한다() {
		conditionUseCase = new WeatherConditionInputPort(tafManagementOutputPort);
		WeatherConditionQuery query = new WeatherConditionQuery(
			"RKSS",
			ofUTC(6, 25, 22, 0),
			new WeatherConditionPredicate(WeatherGroupField.HAS_PHENOMENA, List.of(WeatherPhenomenon.BR))
		);

		Boolean actual = conditionUseCase.execute(query);
		assertEquals(actual, true);
	}

}
