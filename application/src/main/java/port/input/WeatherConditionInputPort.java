package port.input;

import dto.taf.WeatherConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.WeatherConditionPredicate;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.ConditionUseCase;
import vo.taf.Taf;
import vo.weather.WeatherGroup;

@RequiredArgsConstructor
public class WeatherConditionInputPort implements ConditionUseCase<WeatherConditionQuery> {

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
