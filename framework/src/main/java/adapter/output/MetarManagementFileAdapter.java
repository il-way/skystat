package adapter.output;

import dto.RetrievalPeriod;
import parser.metar.MetarParser;
import port.output.MetarManagementOutputPort;
import vo.metar.Metar;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MetarManagementFileAdapter implements MetarManagementOutputPort {



	@Override
	public void save(Metar metar) {

	}

	@Override
	public Metar findByIcao(String icao, ZonedDateTime reportTime) {
		return null;
	}

	@Override
	public List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period) {
		return List.of();
	}

	private static List<Metar> load(Path path) throws IOException {
		YearMonth baseYearMonth = YearMonth.now();
		MetarParser metarParser = new MetarParser(baseYearMonth);

		List<Metar> result = new ArrayList<>();
		try (InputStream in = Files.newInputStream(path)) {

		}

		return result;
	}

}
