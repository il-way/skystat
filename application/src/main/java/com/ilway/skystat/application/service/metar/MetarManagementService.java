package com.ilway.skystat.application.service.metar;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.management.MetarSaveOneCommand;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.application.port.input.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
public class MetarManagementService implements MetarManagementUseCase {

	private final MetarManagementOutputPort metarManagementOutputPort;
	private final MetarParsingOutputPort metarParsingOutputPort;

	@Override
	public void save(MetarSaveOneCommand cmd) {
		Metar metar = metarParsingOutputPort.parse(cmd.rawText(), YearMonth.from(cmd.observationTime()));
		if (!cmd.icao().equalsIgnoreCase(metar.getStationIcao())) {
			throw new BusinessException(400, "ICAO_MISMATCH", "ICAO mismatch: try to save " + cmd.icao() + " but, parsed icao=" + metar.getStationIcao());
		}
		metarManagementOutputPort.save(metar);
	}

	@Override
	public void saveAll(List<Metar> metars) {
		metarManagementOutputPort.saveAll(metars);
	}

	@Override
	public void deleteAllByIcao(String icao) {
		metarManagementOutputPort.deleteAllByIcao(icao);
	}

	@Override
	public List<Metar> findAllByIcao(String icao) {
		return metarManagementOutputPort.findAllByIcao(icao);
	}

	@Override
	public List<Metar> findByIcaoAndObservationTimePeriod(String icao, RetrievalPeriod period) {
		return metarManagementOutputPort.findByIcaoAndObservationTimePeriod(icao, period);
	}

	@Override
	public List<Metar> findByIcaoAndReportTimePeriod(String icao, RetrievalPeriod period) {
		return metarManagementOutputPort.findByIcaoAndReportTimePeriod(icao, period);
	}

}
