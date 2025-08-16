package adapter.output;

import dto.RetrievalPeriod;
import lombok.RequiredArgsConstructor;
import model.generic.IntervalInclusion;
import org.springframework.core.io.Resource;
import parser.metar.MetarParser;
import port.output.MetarManagementOutputPort;
import vo.metar.Metar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MetarManagementResourceFileAdapter implements MetarManagementOutputPort {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final Resource resourceDir;
	private final MetarParser parser = new MetarParser(YearMonth.now());

	@Override
	public void save(Metar metar) {

	}

	@Override
	public List<Metar> findAllByIcao(String icao) {
		try (var reader = new BufferedReader(
			new InputStreamReader(resolveResource(icao).getInputStream(), StandardCharsets.UTF_8)
		)) {
			return reader.lines()
				       .map(String::trim)
				       .filter(s -> !s.isEmpty() && !s.startsWith("#"))
				       .map(MetarManagementResourceFileAdapter::parseResourceFile)
				       .filter(Objects::nonNull)
				       .flatMap(this::parseMetarSafely)
				       .sorted(Comparator.comparing(Metar::getObservationTime))
				       .toList();
		} catch (IOException e) {
			return List.of();
		}
	}

	@Override
	public List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period) {
		return findAllByIcao(icao).stream()
				.filter(m -> IntervalInclusion.CLOSED.test(m.getReportTime(), period.from(), period.to()))
				.toList();
	}

	private static MetarRow parseResourceFile(String line) {
		String[] parts = line.split(",", 3);
		String icao = parts[0].trim();
		String ts = parts[1].trim();
		String raw = parts[2].trim();

		try {
			LocalDateTime time = LocalDateTime.parse(ts, FMT);
			return new MetarRow(icao, time, raw);
		} catch (Exception e) {
			return null;
		}
	}

	private Stream<Metar> parseMetarSafely(MetarRow r) {
		try {
			parser.setYearMonth(YearMonth.from(r.time()));
			return Stream.ofNullable(parser.parse(r.raw()));
		} catch (Exception e) {
			return Stream.empty();
		}
	}

	record MetarRow(String icao, LocalDateTime time, String raw){}

	private Resource resolveResource(String icao) throws IOException {
		String safe = Objects.requireNonNull(icao, "icao").trim().toUpperCase();
		Resource file = resourceDir.createRelative(safe + ".txt");
		if (!file.exists()) {
			throw new FileNotFoundException("Resource not found: " + resourceDir.getURI().getPath() + safe + ".txt");
		}

		return file;
	}

}
