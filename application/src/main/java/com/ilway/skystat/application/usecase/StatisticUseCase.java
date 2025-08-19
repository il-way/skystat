package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;

public interface StatisticUseCase<T> {

	ObservationStatisticResponse execute(T query);

}
