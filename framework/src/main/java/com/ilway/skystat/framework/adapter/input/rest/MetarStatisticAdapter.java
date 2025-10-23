package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticQuery;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;
import com.ilway.skystat.application.model.weather.*;
import com.ilway.skystat.application.usecase.MetarInventoryUseCase;
import com.ilway.skystat.application.usecase.TemperatureStatisticUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import com.ilway.skystat.framework.adapter.input.rest.response.ObservationStatisticResponse;
import com.ilway.skystat.framework.adapter.input.rest.response.TemperatureStatisticResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.generic.Comparison;
import static org.springframework.format.annotation.DateTimeFormat.ISO;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ilway.skystat.application.usecase.StatisticUseCase;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;

import java.time.ZonedDateTime;
import java.util.List;

@RequestMapping("/metar/statistic")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarStatisticAdapter {

	private final MetarInventoryUseCase inventoryUseCase;
	private final StatisticUseCase<ThresholdStatisticQuery> thresholdUseCase;
	private final StatisticUseCase<WeatherStatisticQuery> weatherUseCase;
	private final StatisticUseCase<CloudStatisticQuery> cloudUseCase;
	private final TemperatureStatisticUseCase temperatureUseCase;

	@GetMapping("/threshold")
	public ResponseEntity<ObservationStatisticResponse> getThresholdStatistic(
		@RequestParam("icao") String icao,
		@RequestParam("field") MetricField field,
		@RequestParam("comparison") Comparison comparison,
		@RequestParam("threshold") @NotNull double threshold,
		@RequestParam("unit") Unit unit,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		RetrievalPeriod period = new RetrievalPeriod(st, ed);

		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			icao,
			period,
			new ThresholdCondition(field, comparison, threshold, unit)
		);

		ObservationStatisticResult result = thresholdUseCase.execute(query);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(ObservationStatisticResponse.from(
							 periodInventory, result
			       ));

	}

	@GetMapping("/weather")
	public ResponseEntity<ObservationStatisticResponse> getWeatherStatistic(
		@RequestParam("icao") String icao,
		@RequestParam("condition") WeatherConditionPredicate condition,
		@RequestParam("list") List<WeatherDescription> list,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		WeatherStatisticQuery query = new WeatherStatisticQuery(
			icao,
			period,
			new WeatherCondition(condition, list)
		);

		ObservationStatisticResult result = weatherUseCase.execute(query);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(ObservationStatisticResponse.from(
							 periodInventory, result
			       ));
	}

	@GetMapping("/cloud")
	public ResponseEntity<ObservationStatisticResponse> getCloudStatistic(
		@RequestParam("icao") String icao,
		@RequestParam("condition") CloudConditionPredicate condition,
		@RequestParam("target") WeatherDescription target,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime ed
	) {
		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		CloudStatisticQuery query = new CloudStatisticQuery(
			icao,
			period,
			new CloudCondition(condition, target)
		);

		ObservationStatisticResult result = cloudUseCase.execute(query);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(ObservationStatisticResponse.from(
				       periodInventory, result
			       ));
	}

	@GetMapping("/temperature")
	public ResponseEntity<TemperatureStatisticResponse> getTemperatureStatistic(
		@RequestParam("icao") String icao,
		@RequestParam("startYear") @NotNull Integer startYear,
		@RequestParam("endYear") @NotNull Integer endYear
	) {
		RetrievalPeriod period = RetrievalPeriod.of(startYear, (endYear - startYear));
		TemperatureStatisticQuery query = new TemperatureStatisticQuery(icao, period);

		TemperatureStatisticResult result = temperatureUseCase.execute(query);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(TemperatureStatisticResponse.from(
							 periodInventory, result
			       ));
	}


}
