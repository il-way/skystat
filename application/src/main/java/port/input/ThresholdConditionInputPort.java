package port.input;

import dto.taf.ThresholdConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.ThresholdCondition;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.ConditionUseCase;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import vo.unit.LengthUnit;


@RequiredArgsConstructor
public class ThresholdConditionInputPort implements ConditionUseCase<ThresholdConditionQuery> {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public boolean execute(ThresholdConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		WeatherSnapshot weatherSnapshot = expander.expand(taf)
			                                   .get(query.targetTime());

		ThresholdCondition condition = query.condition();
		double value = condition.field().extract(weatherSnapshot, condition.unit());

		return condition.comparison().test(value, condition.threshold());
	}
}
