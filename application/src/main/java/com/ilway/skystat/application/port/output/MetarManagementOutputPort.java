package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.util.List;

public interface MetarManagementOutputPort {

	void save(Metar metar);

	List<Metar> findAllByIcao(String icao);

	List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period);

}
