package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.usecase.BasicStatisticUseCase;
import com.ilway.skystat.application.usecase.MetarInventoryUseCase;
import com.ilway.skystat.framework.adapter.input.rest.response.AverageSummaryResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarBasicStatisticAdapter {

	private final BasicStatisticUseCase basicStatisticUseCase;
	private final MetarInventoryUseCase inventoryUseCase;

	@GetMapping("/average/summary/{icao}")
	public ResponseEntity<AverageSummaryResponse> getAverageSummary(
		@PathVariable("icao") String icao,
	  @RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
	  @RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed) {

		RetrievalPeriod period = new RetrievalPeriod(st, ed);
		AverageSummary averageSummary = basicStatisticUseCase.averageSummary(icao, period);
		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);

		return ResponseEntity.ok()
			       .body(AverageSummaryResponse.from(periodInventory, averageSummary));
	}


}
