package port.input;

import dto.query.ThresholdConditionQuery;
import dto.query.WeatherConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.ThresholdCondition;
import model.weather.WeatherConditionPredicate;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.WeatherConditionUseCase;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import vo.weather.WeatherGroup;

@RequiredArgsConstructor
public class WeatherConditionInputPort implements WeatherConditionUseCase {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public Boolean execute(WeatherConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		WeatherGroup weatherGroup = expander.expand(taf)
			                            .get(query.targetTime())
			                            .getWeatherGroup();

		WeatherConditionPredicate condition = query.condition();
		return condition.field().test(weatherGroup, condition.target());
	}

}
