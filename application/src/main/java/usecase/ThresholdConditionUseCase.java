package usecase;

import dto.query.ThresholdConditionQuery;

public interface ThresholdConditionUseCase {

	Boolean execute(ThresholdConditionQuery query);

}
