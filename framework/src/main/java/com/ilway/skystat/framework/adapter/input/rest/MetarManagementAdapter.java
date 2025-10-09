package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarFileUploadForm;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarSaveForm;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse.DuplicatedItem;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse.ParsedErrorItem;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ilway.skystat.framework.adapter.output.resource.ResourceFileOperation.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneOffset.UTC;

@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarManagementAdapter {

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final MetarManagementUseCase metarManagementUseCase;

	@PostMapping("/save/{icao}")
	public ResponseEntity<MetarSaveResponse> save(
		@PathVariable("icao") String icao,
		@RequestBody @Validated MetarSaveForm form
	) {
		ZonedDateTime observationTime = form.getObservationTime();
		MetarParser parser = new MetarParser(YearMonth.from(observationTime));
		Metar metar = parser.parse(form.getRawText());
		metarManagementUseCase.save(metar);

		return ResponseEntity.ok().body(
			MetarSaveResponse.success(1, null, null)
		);
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
			ParseBatch batch = parseUpload(reader, parser);
			if (batch.success().isEmpty()) {
				return ResponseEntity.ok().body(MetarSaveResponse.success(
					0, batch.errors(), List.of()
				));
			}

			List<DuplicatedItem> duplicatedItems = new ArrayList<>();
			Set<UniqueKey> existingKeys = findExistingKeys(icao, batch.fromInclusive(), batch.toExclusive());
			Set<UniqueKey> seen = new HashSet<>();
			List<Metar> toInsertMetars = batch.success().stream()
				                           .filter(p -> {
					                           UniqueKey uk = UniqueKey.from(p.metar());
					                           if (!seen.add(uk) || existingKeys.contains(uk)) {
						                           duplicatedItems.add(new DuplicatedItem(p.lineNo()));
						                           return false;
					                           }
					                           return true;
				                           }).map(ParseResult::metar)
				                           .toList();

			metarManagementUseCase.saveAll(toInsertMetars);

			return ResponseEntity.ok().body(
				MetarSaveResponse.success(toInsertMetars.size(), batch.errors(), duplicatedItems)
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read upload file", e);
		}
	}

	private Set<UniqueKey> findExistingKeys(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive) {
		List<Metar> exists = metarManagementUseCase.findByIcaoAndObservationTimePeriod(icao, new RetrievalPeriod(fromInclusive, toExclusive.plusSeconds(1)));
		return exists.stream().map(UniqueKey::from)
			       .collect(Collectors.toSet());
	}

	private ParseBatch parseUpload(BufferedReader reader, MetarParser parser) throws IOException {
		int lineNo = 0;
		String line;

		List<ParseResult> successes = new ArrayList<>();
		List<ParsedErrorItem> errors = new ArrayList<>();
		ZonedDateTime fromInclusive = null;
		ZonedDateTime toExclusive = null;

		while ((line = reader.readLine()) != null) {
			lineNo++;
			String trimmed = line.trim();
			if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

			var metarRow = parseUploadFile(lineNo, line, FMT);
			if (metarRow == null) continue;

			ParseResult pr = parseMetarSafely(metarRow, parser);
			if (pr.isSuccess()) {
				ZonedDateTime t = pr.metar().getObservationTime();
				if (fromInclusive == null || t.isBefore(fromInclusive)) fromInclusive = t;
				if (toExclusive == null || t.isAfter(toExclusive)) toExclusive = t;
				successes.add(pr);
			} else {
				errors.add(new ParsedErrorItem(pr.lineNo(), pr.error().rawText(), pr.error().errorMessage()));
			}
		}

		return new ParseBatch(successes, errors, fromInclusive, toExclusive);
	}

	private record UniqueKey(String icao, Instant obsTime, String rawText) {
		public static UniqueKey from(Metar m) {
			return new UniqueKey(m.getStationIcao(), m.getObservationTime().toInstant(), m.getRawText());
		}
	}

	private record ParseBatch(
		List<ParseResult> success,
		List<ParsedErrorItem> errors,
		ZonedDateTime fromInclusive,
		ZonedDateTime toExclusive
	) {
	}

}
