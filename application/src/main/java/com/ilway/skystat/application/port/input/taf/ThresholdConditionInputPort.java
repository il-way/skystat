package com.ilway.skystat.application.port.input.taf;

import com.ilway.skystat.application.dto.taf.ThresholdConditionQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.ThresholdCondition;
import com.ilway.skystat.application.port.output.TafManagementOutputPort;
import com.ilway.skystat.domain.service.TafSnapshotExpander;
import com.ilway.skystat.application.usecase.ConditionUseCase;
import com.ilway.skystat.domain.vo.taf.Taf;
import com.ilway.skystat.domain.vo.taf.field.WeatherSnapshot;


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
