package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarManagementOutputPort {

	void save(Metar metar);

	void saveAll(List<Metar> metars);

	List<Metar> findAllByIcao(String icao);

	List<Metar> findByIcaoAndObservationTimePeriod(String icao, RetrievalPeriod period);

	List<Metar> findByIcaoAndReportTimePeriod(String icao, RetrievalPeriod period);

}
