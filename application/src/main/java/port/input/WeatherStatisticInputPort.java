package port.input;

import dto.statistic.ObservationStatisticResponse;
import dto.statistic.WeatherStatisticQuery;
import lombok.RequiredArgsConstructor;
import model.weather.WeatherConditionPredicate;
import port.input.internal.ObservationStatisticAggregator;
import port.output.MetarManagementOutputPort;
import usecase.WeatherStatisticUseCase;
import vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class WeatherStatisticInputPort implements WeatherStatisticUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(WeatherStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		WeatherConditionPredicate condition = query.condition();
		Predicate<Metar> predicate = m -> condition.field().test(m.getWeatherGroup(), condition.target());

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
