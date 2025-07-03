package port.input;

import dto.statistic.CloudStatisticQuery;
import dto.statistic.ObservationStatisticResponse;
import lombok.RequiredArgsConstructor;
import model.weather.CloudConditionPredicate;
import port.input.internal.ObservationStatisticAggregator;
import port.output.MetarManagementOutputPort;
import usecase.StatisticUseCase;
import vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CloudStatisticInputPort implements StatisticUseCase<CloudStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(CloudStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		CloudConditionPredicate condition = query.condition();
		Predicate<Metar> predicate = m -> condition.field().test(m.getCloudGroup(), condition.target());

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
