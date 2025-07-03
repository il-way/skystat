package port.input;

import dto.taf.CloudConditionQuery;
import lombok.RequiredArgsConstructor;
import model.weather.CloudConditionPredicate;
import port.output.TafManagementOutputPort;
import service.TafSnapshotExpander;
import usecase.ConditionUseCase;
import vo.taf.Taf;
import vo.weather.CloudGroup;

@RequiredArgsConstructor
public class CloudConditionInputPort implements ConditionUseCase<CloudConditionQuery> {

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
