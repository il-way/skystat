package port.output;

import dto.RetrievalPeriod;
import vo.metar.Metar;

import java.io.IOException;
import java.util.List;

public interface MetarManagementOutputPort {

	void save(Metar metar);

	List<Metar> findAllByIcao(String icao);

	List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period);

}
