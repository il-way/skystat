package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;

public interface StatisticUseCase<T> {

	ObservationStatisticResult execute(T query);

}
