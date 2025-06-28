package usecase;

import dto.query.WeatherConditionQuery;

public interface WeatherConditionUseCase {

	Boolean execute(WeatherConditionQuery query);

}
