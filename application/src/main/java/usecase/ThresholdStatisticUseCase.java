package usecase;

import dto.statistic.ObservationStatisticResponse;
import dto.statistic.ThresholdStatisticQuery;

public interface ThresholdStatisticUseCase {

	ObservationStatisticResponse execute(ThresholdStatisticQuery query);

}
