package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticQuery;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;

public interface TemperatureStatisticUseCase {

	TemperatureStatisticResult execute(TemperatureStatisticQuery query);

}
