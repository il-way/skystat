package adapter.input.rest;

import dto.RetrievalPeriod;
import dto.statistic.CloudStatisticQuery;
import dto.statistic.ObservationStatisticResponse;
import dto.statistic.ThresholdStatisticQuery;
import dto.statistic.WeatherStatisticQuery;
import dto.taf.CloudConditionQuery;
import lombok.RequiredArgsConstructor;
import model.generic.Comparison;
import static org.springframework.format.annotation.DateTimeFormat.ISO;

import model.weather.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usecase.StatisticUseCase;
import vo.unit.LengthUnit;
import vo.weather.type.WeatherDescription;

import java.time.ZonedDateTime;
import java.util.List;

@RequestMapping("/metar/statistic")
@RestController
@RequiredArgsConstructor
public class MetarStatisticAdapter {

	private final StatisticUseCase<ThresholdStatisticQuery> thresholdUseCase;
	private final StatisticUseCase<WeatherStatisticQuery> weatherUseCase;
	private final StatisticUseCase<CloudStatisticQuery> cloudUseCase;

	@GetMapping("/threshold/{icao}")
	public ResponseEntity<ObservationStatisticResponse> getThresholdStatistic(
		@PathVariable("icao") String icao,
		@RequestParam("field") MetricField field,
		@RequestParam("comparison") Comparison comparison,
		@RequestParam("threshold") double threshold,
		@RequestParam("unit") LengthUnit unit,
		@RequestParam("startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			icao,
			new RetrievalPeriod(st, ed),
			new ThresholdCondition(field, comparison, threshold, unit)
		);

		ObservationStatisticResponse execute = thresholdUseCase.execute(query);
		return ResponseEntity.ok()
			       .body(execute);

	}

	@GetMapping("/weather/{icao}")
	public ResponseEntity<ObservationStatisticResponse> getWeatherStatistic(
		@PathVariable("icao") String icao,
		@RequestParam("condition") WeatherConditionPredicate condition,
		@RequestParam("list") List<WeatherDescription> list,
		@RequestParam("startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		WeatherStatisticQuery query = new WeatherStatisticQuery(
			icao,
			new RetrievalPeriod(st, ed),
			new WeatherCondition(condition, list)
		);

		ObservationStatisticResponse execute = weatherUseCase.execute(query);
		return ResponseEntity.ok()
			       .body(execute);
	}

	@GetMapping("/cloud/{icao}")
	public ResponseEntity<ObservationStatisticResponse> getCloudStatistic(
		@PathVariable("icao") String icao,
		@RequestParam("condition") CloudConditionPredicate condition,
		@RequestParam("list") WeatherDescription target,
		@RequestParam("startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		CloudStatisticQuery query = new CloudStatisticQuery(
			icao,
			new RetrievalPeriod(st, ed),
			new CloudCondition(condition, target)
		);

		ObservationStatisticResponse execute = cloudUseCase.execute(query);
		return ResponseEntity.ok()
			       .body(execute);
	}


}
