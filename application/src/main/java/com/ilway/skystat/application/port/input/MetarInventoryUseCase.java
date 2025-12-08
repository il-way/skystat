package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.inventory.DatasetCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodCoverage;
import com.ilway.skystat.application.dto.inventory.PeriodInventory;

public interface MetarInventoryUseCase {

	/**
	 * 스테이션 전체 데이터셋의 보유 범위와 총 건수.
	 * @param icao ICAO 코드
	 * @return 데이터셋 커버리지
	 */
	DatasetCoverage findDatasetCoverage(String icao);

	PeriodCoverage findPeriodCoverage(String icao, RetrievalPeriod period);

	/**
	 * 사용자 요청 기간에 대해 실제로 존재하는 데이터의 경계와 집계값.
	 * @param icao ICAO 코드
	 * @param period 요청 기간 (fromInclusive ~ toExclusive)
	 * @return 기간별 인벤토리(교집합 구간 + 카운트)
	 */
	PeriodInventory getPeriodInventory(String icao, RetrievalPeriod period);

}
