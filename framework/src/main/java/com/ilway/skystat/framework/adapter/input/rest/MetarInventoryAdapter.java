package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.inventory.DatasetCoverage;
import com.ilway.skystat.application.port.input.MetarInventoryUseCase;
import com.ilway.skystat.framework.adapter.input.rest.response.DatasetCoverageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/metar")
@RestController
@RequiredArgsConstructor
public class MetarInventoryAdapter {

	private final MetarInventoryUseCase inventoryUseCase;

	@GetMapping("/dataset")
	public ResponseEntity<DatasetCoverageResponse> findDatasetCoverage(@RequestParam("icao") String icao) {
		DatasetCoverage datasetCoverage = inventoryUseCase.findDatasetCoverage(icao);

		return ResponseEntity.ok()
			       .body(DatasetCoverageResponse.from(icao, datasetCoverage));
	}

}
