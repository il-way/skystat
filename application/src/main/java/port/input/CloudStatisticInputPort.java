package port.input;

import dto.statistic.CloudStatisticQuery;
import dto.statistic.ObservationStatisticResponse;
import lombok.RequiredArgsConstructor;
import model.CloudConditionPredicate;
import model.WeatherConditionPredicate;
import port.input.internal.ObservationStatisticAggregator;
import port.output.MetarManagementOutputPort;
import usecase.CloudStatisticUseCase;
import usecase.WeatherStatisticUseCase;
import vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CloudStatisticInputPort implements CloudStatisticUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(CloudStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		CloudConditionPredicate condition = query.condition();
		Predicate<Metar> predicate = m -> condition.field().test(m, condition.target());

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
