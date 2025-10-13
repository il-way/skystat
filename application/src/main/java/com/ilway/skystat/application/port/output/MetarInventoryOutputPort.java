package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.DatasetCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodCoverage;

public interface MetarInventoryOutputPort {

	DatasetCoverage findDatasetCoverage(String icao);

	PeriodCoverage findPeriodCoverage(String icao, RetrievalPeriod period);

}
