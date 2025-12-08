package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;
import com.ilway.skystat.application.port.input.MetarInventoryUseCase;
import com.ilway.skystat.application.port.input.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.input.rest.response.WindRoseResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@Validated
@RequestMapping("/api/metar")
@RestController
@RequiredArgsConstructor
public class WindRoseAdapter {

	private final WindRoseUseCase windRoseUseCase;
	private final MetarInventoryUseCase inventoryUseCase;

	@GetMapping("/windrose")
	public ResponseEntity<WindRoseResponse> getDefaultWindRose(
		@RequestParam("icao") String icao,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed
	) {
		RetrievalPeriod period = new RetrievalPeriod(st, ed);

		PeriodInventory periodInventory = inventoryUseCase.getPeriodInventory(icao, period);
		WindRoseResult result = windRoseUseCase.generateDefault(icao, period);
		WindRoseResponse windRoseResponse = WindRoseResponse.fromDefaultWindRose(periodInventory, result);

		return ResponseEntity.ok()
			       .body(windRoseResponse);
	}
}
