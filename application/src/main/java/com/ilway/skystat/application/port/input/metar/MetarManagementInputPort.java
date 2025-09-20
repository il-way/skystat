package com.ilway.skystat.application.port.input.metar;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetarManagementInputPort implements MetarManagementUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;

	@Override
	public void save(Metar metar) {
		metarManagementOutputPort.save(metar);
	}

	@Override
	public void saveAll(List<Metar> metars) {
		metarManagementOutputPort.saveAll(metars);
	}

	@Override
	public List<Metar> findAllByIcao(String icao) {
		return metarManagementOutputPort.findAllByIcao(icao);
	}

	@Override
	public List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period) {
		return metarManagementOutputPort.findByIcaoAndPeriod(icao, period);
	}
}
