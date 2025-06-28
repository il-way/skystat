package port.output;

import vo.taf.Taf;

import java.time.ZonedDateTime;

public interface TafManagementOutputPort {

	void save(Taf taf);

	Taf findByIcao(String icao);

}
