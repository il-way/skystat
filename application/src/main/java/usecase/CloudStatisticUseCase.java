package usecase;

import dto.statistic.CloudStatisticQuery;
import dto.statistic.ObservationStatisticResponse;

public interface CloudStatisticUseCase {

	ObservationStatisticResponse execute(CloudStatisticQuery query);

}
