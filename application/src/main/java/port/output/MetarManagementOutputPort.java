package port.output;

import dto.RetrievalPeriod;
import vo.metar.Metar;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarManagementOutputPort {

	void save(Metar metar);

	Metar findByIcao(String icao, ZonedDateTime reportTime);

	List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period);

}
