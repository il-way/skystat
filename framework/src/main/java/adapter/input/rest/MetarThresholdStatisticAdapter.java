package adapter.input.rest;

import dto.RetrievalPeriod;
import dto.statistic.CloudStatisticQuery;
import dto.statistic.ObservationStatisticResponse;
import dto.statistic.ThresholdStatisticQuery;
import dto.statistic.WeatherStatisticQuery;
import lombok.RequiredArgsConstructor;
import model.generic.Comparison;
import static org.springframework.format.annotation.DateTimeFormat.ISO;

import static model.weather.MetricField.*;

import model.weather.MetricField;
import model.weather.ThresholdCondition;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usecase.StatisticUseCase;
import vo.unit.LengthUnit;
import vo.unit.PressureUnit;
import vo.unit.SpeedUnit;

import static vo.unit.LengthUnit.*;

import java.net.http.HttpResponse;
import java.time.ZonedDateTime;

@RequestMapping("/metar/statistic/threshold")
@RestController
@RequiredArgsConstructor
public class MetarThresholdStatisticAdapter {

	private final StatisticUseCase<ThresholdStatisticQuery> thresholdUseCase;

	@GetMapping("/{icao}")
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

}
