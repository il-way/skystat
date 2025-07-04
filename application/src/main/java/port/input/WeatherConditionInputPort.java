package port.input;

import dto.taf.WeatherConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.WeatherCondition;
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
	public boolean execute(WeatherConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		WeatherGroup weatherGroup = expander.expand(taf)
			                            .get(query.targetTime())
			                            .getWeatherGroup();

		WeatherCondition condition = query.condition();
		return condition.predicate().test(weatherGroup, condition.target());
	}

}
