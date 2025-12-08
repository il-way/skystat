package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;

public interface StatisticUseCase<T> {

	ObservationStatisticResult execute(T query);

}
