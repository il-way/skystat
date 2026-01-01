package com.ilway.skystat.framework.adapter.output.resource;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;
import com.ilway.skystat.application.model.generic.IntervalInclusion;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;

@RequiredArgsConstructor
public class MetarManagementResourceFileAdapter implements MetarManagementOutputPort {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final Resource resourceDir = new ClassPathResource("/data/metar/");

	@Override
	public void save(Metar metar) {

	}

	@Override
	public void saveAll(List<Metar> metars) {

	}

	@Override
	public void deleteAllByIcao(String icao) {

	}

	@Override
	public List<Metar> findAllByIcao(@UppercaseParam String icao) {
		MetarParser parser = new MetarParser(YearMonth.now());

		try (var reader = new BufferedReader(
			new InputStreamReader(resolveResource(icao).getInputStream(), StandardCharsets.UTF_8)
		)) {
			return reader.lines()
				       .map(String::trim)
				       .filter(s -> !s.isEmpty() && !s.startsWith("#"))
				       .map(MetarManagementResourceFileAdapter::parseResourceFile)
				       .filter(Objects::nonNull)
				       .flatMap(row -> parseMetarSafely(row, parser))
				       .sorted(Comparator.comparing(Metar::getObservationTime))
				       .toList();
		} catch (IOException e) {
			return List.of();
		}
	}

	@Override
	public List<Metar> findByIcaoAndObservationTimePeriod(@UppercaseParam String icao, RetrievalPeriod period) {
		return findAllByIcao(icao).stream()
			       .filter(m -> IntervalInclusion.CLOSED.test(m.getReportTime(), period.fromInclusive(), period.toExclusive()))
			       .toList();
	}

	@Override
	public List<Metar> findByIcaoAndReportTimePeriod(@UppercaseParam String icao, RetrievalPeriod period) {
		return findAllByIcao(icao).stream()
				.filter(m -> IntervalInclusion.CLOSED.test(m.getReportTime(), period.fromInclusive(), period.toExclusive()))
				.toList();
	}

	private static MetarRow parseResourceFile(String line) {
		String[] parts = line.split(",", 3);
		String icao = parts[0].trim();
		String ts = parts[1].trim();
		String raw = parts[2].trim();

		try {
			ZonedDateTime time = LocalDateTime.parse(ts, FMT).atZone(UTC);
			return new MetarRow(icao, time, raw);
		} catch (Exception e) {
			return null;
		}
	}

	private static Stream<Metar> parseMetarSafely(MetarRow r, MetarParser parser) {
		try {
			parser.setYearMonth(YearMonth.from(r.time()));
			return Stream.ofNullable(parser.parse(r.raw()));
		} catch (Exception e) {
			return Stream.empty();
		}
	}

	record MetarRow(String icao, ZonedDateTime time, String raw){}

	private Resource resolveResource(String icao) throws IOException {
		String safe = Objects.requireNonNull(icao, "icao").trim().toUpperCase();
		Resource file = resourceDir.createRelative(safe + ".txt");
		if (!file.exists()) {
			throw new FileNotFoundException("Resource not found: " + resourceDir.getURI().getPath() + safe + ".txt");
		}

		return file;
	}

}
