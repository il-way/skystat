package usecase;

import dto.statistic.ObservationStatisticResponse;
import dto.statistic.WeatherStatisticQuery;

public interface WeatherStatisticUseCase {

	ObservationStatisticResponse execute(WeatherStatisticQuery query);

}
