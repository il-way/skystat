package com.ilway.skystat.application.port.input.taf;

import com.ilway.skystat.application.dto.taf.WeatherConditionQuery;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.port.output.TafManagementOutputPort;
import com.ilway.skystat.domain.service.TafSnapshotExpander;
import com.ilway.skystat.application.usecase.ConditionUseCase;
import com.ilway.skystat.domain.vo.taf.Taf;
import com.ilway.skystat.domain.vo.weather.Weathers;

@RequiredArgsConstructor
public class WeatherConditionInputPort implements ConditionUseCase<WeatherConditionQuery> {

	private final TafManagementOutputPort tafManagementOutputPort;
	private final TafSnapshotExpander expander = new TafSnapshotExpander();

	@Override
	public boolean execute(WeatherConditionQuery query) {
		Taf taf = tafManagementOutputPort.findByIcao(query.icao());
		Weathers weathers = expander.expand(taf)
			                            .get(query.targetTime())
			                            .getWeathers();

		WeatherCondition condition = query.condition();
		return condition.predicate().test(weathers, condition.target());
	}

}
