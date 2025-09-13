package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetarManagementMySQLAdapter implements MetarManagementOutputPort {

	private final MetarManagementRepository repository;

	@Override
	public void save(Metar metar) {

	}

	@Override
	public List<Metar> findAllByIcao(String icao) {
		return List.of();
	}

	@Override
	public List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period) {
		return List.of();
	}
}
