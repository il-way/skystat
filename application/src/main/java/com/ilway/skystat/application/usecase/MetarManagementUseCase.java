package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarManagementUseCase {

	void save(Metar metar);

	void saveAll(List<Metar> metars);

	List<Metar> findAllByIcao(String icao, ZonedDateTime reportTime);

	List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period);

}
