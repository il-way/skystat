package port.input;

import dto.statistic.ObservationStatisticResponse;
import dto.statistic.WeatherStatisticQuery;
import lombok.RequiredArgsConstructor;
import model.weather.WeatherCondition;
import port.input.internal.ObservationStatisticAggregator;
import port.output.MetarManagementOutputPort;
import usecase.StatisticUseCase;
import vo.metar.Metar;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class WeatherStatisticInputPort implements StatisticUseCase<WeatherStatisticQuery> {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public ObservationStatisticResponse execute(WeatherStatisticQuery query) {
		List<Metar> metarList = metarManagementOutputPort.findByIcaoAndPeriod(query.icao(), query.period());

		WeatherCondition condition = query.condition();
		Predicate<Metar> predicate = m -> condition.predicate().test(m.getWeatherGroup(), condition.target());

		return ObservationStatisticAggregator.aggregate(metarList, predicate);
	}
}
