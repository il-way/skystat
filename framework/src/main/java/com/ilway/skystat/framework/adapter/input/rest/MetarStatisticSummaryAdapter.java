package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryTarget;
import com.ilway.skystat.application.port.input.MetarInventoryUseCase;
import com.ilway.skystat.application.port.input.StatisticSummaryUseCase;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.input.rest.response.AverageSummaryResponse;
import com.ilway.skystat.framework.adapter.input.rest.response.MonthlyCountSummaryResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RequestMapping("/api/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarStatisticSummaryAdapter {

	private final StatisticSummaryUseCase statisticSummaryUseCase;
	private final MetarInventoryUseCase inventoryUseCase;

	@GetMapping("/summary/average")
	public ResponseEntity<AverageSummaryResponse> getAverageSummary(
		@RequestParam("icao") String icao,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed) {

		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		AverageSummary averageSummary = statisticSummaryUseCase.averageSummary(icao, period);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(AverageSummaryResponse.from(periodInventory, averageSummary));
	}

	@GetMapping("/summary/count")
	public ResponseEntity<MonthlyCountSummaryResponse> getMonthlyCountSummary(
		@RequestParam("icao") String icao,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed,
		@RequestParam("windPeakThreshold") Double windPeakThreshold,
		@RequestParam("visibilityThreshold") Double visibilityThreshold,
		@RequestParam("ceilingThreshold") Double ceilingThreshold,
		@RequestParam("phenomenon") WeatherPhenomenon phenomenon,
		@RequestParam("descriptor") WeatherDescriptor descriptor) {

		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		MonthlyCountSummaryTarget target = new MonthlyCountSummaryTarget(windPeakThreshold, visibilityThreshold, ceilingThreshold, phenomenon, descriptor);
		List<MonthlyCountSummaryDto> monthly = statisticSummaryUseCase.countSummaryByMonth(icao, period, target);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(MonthlyCountSummaryResponse.from(periodInventory, monthly));
	}

}
