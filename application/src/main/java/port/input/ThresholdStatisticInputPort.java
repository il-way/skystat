package port.input;

import dto.statistic.ObservationStatisticResponse;
import dto.statistic.ThresholdStatisticQuery;
import lombok.RequiredArgsConstructor;
import model.weather.ThresholdCondition;
import port.input.internal.ObservationStatisticAggregator;
import port.output.MetarManagementOutputPort;
import usecase.StatisticUseCase;
import vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ThresholdStatisticInputPort implements StatisticUseCase<ThresholdStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(ThresholdStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		ThresholdCondition condition = query.condition();
		Predicate<Metar> predicate = m -> {
			double value = condition.field().extract(m, condition.unit());
			return condition.comparison().test(value, condition.threshold());
		};

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
