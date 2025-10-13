package com.ilway.skystat.framework.adapter.output.mysql.inventory;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.DatasetCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodCoverage;
import com.ilway.skystat.application.port.output.MetarInventoryOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.CoverageQueryDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MetarInventoryMySqlAdapter implements MetarInventoryOutputPort {

	private final MetarInventoryRepository repository;

	@Override
	public DatasetCoverage findDatasetCoverage(String icao) {
		CoverageQueryDto coverage = repository.findDatasetCoverage(icao);
		return new DatasetCoverage(coverage.firstTime(), coverage.lastTime(), coverage.count());
	}

	@Override
	public PeriodCoverage findPeriodCoverage(String icao, RetrievalPeriod period) {
		CoverageQueryDto coverage = repository.findPeriodCoverage(icao, period.fromInclusive(), period.toExclusive());
		return new PeriodCoverage(coverage.firstTime(), coverage.lastTime(), coverage.count());
	}

}
