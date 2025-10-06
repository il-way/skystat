package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarFileUploadForm;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarSaveForm;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ilway.skystat.framework.adapter.output.resource.ResourceFileOperation.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.partitioningBy;

@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarManagementAdapter {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final MetarManagementUseCase metarManagementUseCase;

	record MetarRow(String icao, ZonedDateTime time, String raw) {
	}

	@PostMapping("/save/{icao}")
	public ResponseEntity<MetarSaveResponse> save(
		@PathVariable("icao") String icao,
		@RequestBody @NotNull MetarSaveForm form
	) {
		ZonedDateTime observationTime = form.getObservationTime();
		MetarParser parser = new MetarParser(YearMonth.from(observationTime));
		Metar metar = parser.parse(form.getRawText());
		metarManagementUseCase.save(metar);

		return ResponseEntity.ok()
			       .body(MetarSaveResponse.success(1, 0, List.of()));

	}

	@PostMapping("/save/upload/{icao}")
	public ResponseEntity<MetarSaveResponse> saveAll(
		@PathVariable("icao") String icao,
		@Validated @ModelAttribute MetarFileUploadForm form
	) {
		MetarParser parser = new MetarParser(YearMonth.now());

		try (var reader = new BufferedReader(
			new InputStreamReader(form.getFile().getInputStream(), UTF_8)
		)) {
			Map<Boolean, List<ParseResult>> parseResultMap = reader.lines()
				                                                 .map(String::trim)
				                                                 .filter(s -> !s.isEmpty() && !s.startsWith("#"))
				                                                 .map(line -> parseUploadFile(line, FMT))
				                                                 .filter(Objects::nonNull)
				                                                 .map(r -> parseMetarSafely(r, parser))
				                                                 .collect(
					                                                 partitioningBy(ParseResult::isSuccess)
				                                                 );

			List<Metar> parsedMetars = parseResultMap.get(true).stream()
				                           .map(ParseResult::metar)
				                           .toList();

			List<ParseError> errorList = parseResultMap.get(false).stream()
				                             .map(ParseResult::error)
				                             .toList();

			metarManagementUseCase.saveAll(parsedMetars);

			return ResponseEntity.ok().body(
				MetarSaveResponse.success(parsedMetars.size(), errorList.size(), errorList)
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read upload file", e);
		}
	}

}
