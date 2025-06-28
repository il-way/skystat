package port.input;

import dto.query.ThresholdConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.ThresholdCondition;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.ThresholdConditionUseCase;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;


@RequiredArgsConstructor
public class ThresholdConditionInputPort implements ThresholdConditionUseCase {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public Boolean execute(ThresholdConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		WeatherSnapshot weatherSnapshot = expander.expand(taf)
			                                   .get(query.targetTime());

		ThresholdCondition condition = query.condition();
		double value = condition.field().extract(weatherSnapshot, condition.unit());

		return condition.comparison().test(value, condition.threshold());
	}
}
