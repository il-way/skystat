package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.input.rest.response.WindRoseResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@Validated
@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
public class WindRoseAdapter {

	private final WindRoseUseCase windRoseUseCase;

	@GetMapping("/windrose/{icao}")
	public ResponseEntity<WindRoseResponse> getDefaultWindRose(
		@PathVariable("icao") String icao,
		@RequestParam("startDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed
	) {
		WindRoseResult result = windRoseUseCase.generateDefaultMonthlyWindRose(icao, new RetrievalPeriod(st, ed));
		WindRoseResponse windRoseResponse = WindRoseResponse.fromDefaultWindRose(result);

		return ResponseEntity.ok()
			       .body(windRoseResponse);
	}
}
