package port.input;

import dto.query.CloudConditionQuery;
import dto.query.WeatherConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.CloudConditionPredicate;
import model.weather.WeatherConditionPredicate;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.CloudConditionUseCase;
import vo.taf.Taf;
import vo.weather.CloudGroup;
import vo.weather.WeatherGroup;

@RequiredArgsConstructor
public class CloudConditionInputPort implements CloudConditionUseCase {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public Boolean execute(CloudConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		CloudGroup cloudGroup = expander.expand(taf)
			                        .get(query.targetTime())
			                        .getCloudGroup();

		CloudConditionPredicate condition = query.condition();
		return condition.field().test(cloudGroup, condition.target());
	}

}
