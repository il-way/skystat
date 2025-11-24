package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyAverageDto;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.model.weather.MetricField;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.application.usecase.MetarInventoryUseCase;
import com.ilway.skystat.domain.vo.unit.Unit;
import com.ilway.skystat.framework.adapter.input.rest.response.AverageMonthlyResponse;
import com.ilway.skystat.framework.adapter.input.rest.response.AverageSummaryResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RequestMapping("/api/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarBasicStatisticAdapter {

	private final BasicStatisticUseCase basicStatisticUseCase;
	private final MetarInventoryUseCase inventoryUseCase;

	@GetMapping("/average")
	public ResponseEntity<AverageMonthlyResponse> getAverageMonthly(
		@RequestParam("icao") String icao,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed,
		@RequestParam("field") @NotNull MetricField field,
		@RequestParam("unit") @NotNull Unit unit) {

		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);
		List<MonthlyAverageDto> monthly = basicStatisticUseCase.averageMonthly(icao, period, field, unit);

		return ResponseEntity.ok()
			       .body(AverageMonthlyResponse.from(periodInventory, monthly));
	}


}
