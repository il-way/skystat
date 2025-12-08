package com.ilway.skystat.application.port.input;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.management.MetarSaveOneCommand;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;

public interface MetarManagementUseCase {

	void save(MetarSaveOneCommand cmd);

	void saveAll(List<Metar> metars);

	List<Metar> findAllByIcao(String icao);

	List<Metar> findByIcaoAndObservationTimePeriod(String icao, RetrievalPeriod period);

	List<Metar> findByIcaoAndReportTimePeriod(String icao, RetrievalPeriod period);

}
