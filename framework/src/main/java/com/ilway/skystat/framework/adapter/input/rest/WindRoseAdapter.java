package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.application.dto.windrose.WindRoseResult;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.framework.adapter.input.rest.response.WindRoseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RequestMapping("/windrose")
@RestController
@RequiredArgsConstructor
public class WindRoseAdapter {

	private final WindRoseUseCase windRoseUseCase;

	@GetMapping("/{icao}")
	public ResponseEntity<WindRoseResponse> getWindRose(
		@PathVariable("icao") String icao,
		@RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime st,
		@RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime ed
	) {
		List<SpeedBin> speedBins = SpeedBin.of5KtSpeedBins();
		List<DirectionBin> directionBins = DirectionBin.of16DirectionBins();
		WindRoseResult result = windRoseUseCase.generateMonthlyWindRose(
			icao,
			new RetrievalPeriod(st, ed),
			speedBins,
			directionBins
		);

		WindRoseResponse windRoseResponse = WindRoseResponse.from(speedBins, directionBins, result);
		return ResponseEntity.ok()
			       .body(windRoseResponse);
	}
}
