package com.ilway.skystat.application.port.input.metar;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.DatasetCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;
import com.ilway.skystat.application.port.output.MetarInventoryOutputPort;
import com.ilway.skystat.application.usecase.MetarInventoryUseCase;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
public class MetarInventoryInputPort implements MetarInventoryUseCase {

	private final MetarInventoryOutputPort outputPort;

	@Override
	public DatasetCoverage findDatasetCoverage(String icao) {
		return outputPort.findDatasetCoverage(icao);
	}

	@Override
	public PeriodCoverage findPeriodCoverage(String icao, RetrievalPeriod period) {
		return outputPort.findPeriodCoverage(icao, period);
	}

	@Override
	public PeriodInventory getPeriodInventory(String icao, RetrievalPeriod period) {
		DatasetCoverage datasetCoverage = findDatasetCoverage(icao);
		PeriodCoverage periodCoverage = findPeriodCoverage(icao, period);

		if (datasetCoverage == null || !datasetCoverage.hasData()) {
			return PeriodInventory.empty(icao, period, null, null);
		}

		if (periodCoverage == null || !periodCoverage.hasData()) {
			return PeriodInventory.empty(icao, period, datasetCoverage.firstReportTime(), datasetCoverage.lastReportTime());
		}

		return PeriodInventory.builder()
			       .icao(icao)
			       .requestPeriod(period)
			       .firstReportTime(datasetCoverage.firstReportTime())
			       .lastReportTime(datasetCoverage.lastReportTime())
			       .firstAvailableTime(periodCoverage.firstInPeriod())
			       .lastAvailableTime(periodCoverage.lastInPeriod())
			       .totalCount(periodCoverage.totalCount())
			       .build();
	}

}
