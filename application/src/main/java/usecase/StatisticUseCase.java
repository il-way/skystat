package usecase;

import dto.statistic.ObservationStatisticResponse;

public interface StatisticUseCase<T> {

	ObservationStatisticResponse execute(T query);

}
