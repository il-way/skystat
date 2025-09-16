package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarFileUploadForm;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarSaveForm;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneOffset.UTC;

@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
public class MetarManagementAdapter {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final MetarManagementUseCase metarManagementUseCase;

	record MetarRow(String icao, ZonedDateTime time, String raw){}

	@PostMapping("/save/{icao}")
	public ResponseEntity<String> save(
		@PathVariable("icao") String icao,
		@RequestBody MetarSaveForm form
		) {
		ZonedDateTime observationTime = form.getObservationTime();
		MetarParser parser = new MetarParser(YearMonth.from(observationTime));
		Metar metar = parser.parse(form.getRawText());
		metarManagementUseCase.save(metar);

		return ResponseEntity.ok("");
	}

	@PostMapping("/save/upload/{icao}")
	public ResponseEntity<String> saveAll(
		@PathVariable("icao") String icao,
		@Validated @ModelAttribute MetarFileUploadForm form
	) {
		MetarParser parser = new MetarParser(YearMonth.now());

		try (var reader = new BufferedReader(
			new InputStreamReader(form.getFile().getInputStream(), UTF_8)
		)) {
			List<Metar> metars = reader.lines()
				                   .map(String::trim)
				                   .filter(s -> !s.isEmpty() && !s.startsWith("#"))
				                   .map(MetarManagementAdapter::parseUploadFile)
				                   .filter(Objects::nonNull)
				                   .flatMap(row -> parseMetarSafely(row, parser))
				                   .sorted(Comparator.comparing(Metar::getObservationTime))
				                   .toList();

			metarManagementUseCase.saveAll(metars);

		} catch (IOException e) {
			return ResponseEntity.internalServerError()
				       .body("Failed to read the file");
		}

		return ResponseEntity.ok("");
	}

	private static MetarRow parseUploadFile(String line) {
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

}
