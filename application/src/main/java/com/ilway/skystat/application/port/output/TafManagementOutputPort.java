package com.ilway.skystat.application.port.output;

import com.ilway.skystat.domain.vo.taf.Taf;

public interface TafManagementOutputPort {

	void save(Taf taf);

	Taf findByIcao(String icao);

}
