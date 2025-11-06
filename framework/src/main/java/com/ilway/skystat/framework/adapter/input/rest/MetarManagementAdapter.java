package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.management.MetarSaveOneCommand;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarFileUploadForm;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarSaveForm;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse.DuplicatedItem;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse.ParsedErrorItem;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ilway.skystat.framework.adapter.output.resource.ResourceFileOperation.*;
import static java.nio.charset.StandardCharsets.UTF_8;

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
		MetarSaveOneCommand cmd = new MetarSaveOneCommand(icao, form.getRawText(), form.getObservationTime());
		metarManagementUseCase.save(cmd);

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

			List<ParsedErrorItem> errorItems = new ArrayList<>(batch.errors());
			List<DuplicatedItem> duplicatedItems = new ArrayList<>();
			Set<UniqueKey> existingKeys = findExistingKeys(icao, batch.fromInclusive(), batch.toExclusive());
			Set<UniqueKey> seen = new HashSet<>();
			List<Metar> toInsertMetars = batch.success().stream()
				                             .filter(getIcaoPredicate(icao, errorItems))
				                             .filter(getDuplicatePredicate(seen, existingKeys, duplicatedItems))
				                             .map(ParseResult::metar)
				                             .toList();

			metarManagementUseCase.saveAll(toInsertMetars);

			return ResponseEntity.ok().body(
				MetarSaveResponse.success(toInsertMetars.size(), errorItems, duplicatedItems)
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read upload file", e);
		}
	}

	private Predicate<ParseResult> getDuplicatePredicate(Set<UniqueKey> seen, Set<UniqueKey> existingKeys, List<DuplicatedItem> duplicatedItems) {
		return p -> {
			UniqueKey uk = UniqueKey.from(p.metar());
			if (!seen.add(uk) || existingKeys.contains(uk)) {
				duplicatedItems.add(new DuplicatedItem(p.lineNo()));
				return false;
			}
			return true;
		};
	}

	private Predicate<ParseResult> getIcaoPredicate(String icao, List<ParsedErrorItem> errorItems) {
		return p -> {
			if (!validateIcao(icao, p.metar().getStationIcao())) {
				errorItems.add(new ParsedErrorItem(
					p.lineNo(),
					p.metar().getRawText(),
					"ICAO mismatch: try to save " + icao + " but, parsed icao=" + p.metar().getStationIcao())
				);
				return false;
			}
			return true;
		};
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

	private boolean validateIcao(String pathIcao, String parsedIcao) {
		String path = pathIcao == null ? "" : pathIcao.trim().toUpperCase(Locale.ROOT);
		String parsed = parsedIcao == null ? "" : parsedIcao.trim().toUpperCase(Locale.ROOT);
		return !path.isEmpty() && path.equals(parsed);
	}

}
