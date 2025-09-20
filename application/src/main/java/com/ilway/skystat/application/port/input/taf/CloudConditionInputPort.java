package com.ilway.skystat.application.port.input.taf;

import com.ilway.skystat.application.dto.taf.CloudConditionQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.CloudCondition;
import com.ilway.skystat.application.port.output.TafManagementOutputPort;
import com.ilway.skystat.domain.service.TafSnapshotExpander;
import com.ilway.skystat.application.usecase.ConditionUseCase;
import com.ilway.skystat.domain.vo.taf.Taf;
import com.ilway.skystat.domain.vo.weather.CloudGroup;

@RequiredArgsConstructor
public class CloudConditionInputPort implements ConditionUseCase<CloudConditionQuery> {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public boolean execute(CloudConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		CloudGroup cloudGroup = expander.expand(taf)
			                        .get(query.targetTime())
			                        .getCloudGroup();

		CloudCondition condition = query.condition();
		return condition.predicate().test(cloudGroup, condition.target());
	}

}
