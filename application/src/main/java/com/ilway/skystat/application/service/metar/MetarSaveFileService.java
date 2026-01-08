package com.ilway.skystat.application.service.metar;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.management.*;
import com.ilway.skystat.application.exception.BusinessException;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.port.output.MetarParsingOutputPort;
import com.ilway.skystat.application.port.input.MetarSaveFileUseCase;
import com.ilway.skystat.domain.policy.rounding.RoundingPolicy;
import com.ilway.skystat.domain.vo.metar.Metar;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MetarSaveFileService implements MetarSaveFileUseCase {

	private final MetarManagementOutputPort managementOutputPort;
	private final MetarParsingOutputPort parsingOutputPort;
	private static final Pattern OBS_TIME_PATTERN = Pattern.compile("(\\d{4}-\\d{2})-\\d{2} \\d{2}:\\d{2}");
	private static final Pattern RAW_TEXT_PATTERN = Pattern.compile("\\w{4} \\d{6}Z [\\w\\W]+");

	@Override
	public MetarSaveFileResult saveFile(MetarSaveFileCommand cmd) {
		List<ParsingErrorItem> parsingErrors = new ArrayList<>();
		List<DuplicatedItem> duplicates = new ArrayList<>();
		List<InsertMetar> toInsertMetars = new ArrayList<>();

		ZonedDateTime fromInclusive = null;
		ZonedDateTime toExclusive = null;
		Set<UniqueKey> seen = new HashSet<>();

		int effectiveLines = 0;
		try (var reader = new BufferedReader(new InputStreamReader(cmd.content(), cmd.charset()))){
			String line;
			int lineNo = 0;
			while((line = reader.readLine()) != null) {
				lineNo++;

				if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
				effectiveLines++;

				try {
					YearMonth ym = extractYearMonth(line);
					String rawText = extractRawText(line);
					Metar m = parsingOutputPort.parse(rawText, ym);
					if (!cmd.icao().equalsIgnoreCase(m.getStationIcao())) {
						parsingErrors.add(new ParsingErrorItem(
							lineNo,
							m.getRawText(),
							"ICAO mismatch: try to save " + cmd.icao() + " but, parsed icao=" + m.getStationIcao()
						));
						continue;
					}

					ZonedDateTime obsTime = m.getObservationTime();
					if (fromInclusive == null || obsTime.isBefore(fromInclusive)) fromInclusive = obsTime;
					if (toExclusive == null || obsTime.isAfter(toExclusive)) toExclusive = obsTime;

					toInsertMetars.add(new InsertMetar(lineNo, m));
				} catch (Exception e) {
					parsingErrors.add(new ParsingErrorItem(lineNo, line, e.getMessage()));
				}
			}
		} catch (IOException e) {
			parsingErrors.add(new ParsingErrorItem(-1, cmd.fileName(), "IO_ERROR: " + e.getMessage()));
			return new MetarSaveFileResult(0, 1, 0, 0, parsingErrors, duplicates, "I/O error.");
		}

		double parsingErrorRate = effectiveLines > 0 ? RoundingPolicy.ofDefault().apply(parsingErrors.size() / (double) effectiveLines) : 1d;
		if (toInsertMetars.isEmpty()) {
			return new MetarSaveFileResult(0, parsingErrors.size(), 0, parsingErrorRate, parsingErrors, duplicates, "METAR is empty or already exists.");
		}

		RetrievalPeriod period = new RetrievalPeriod(fromInclusive, toExclusive.plusMinutes(1));
		Set<UniqueKey> existing = managementOutputPort.findByIcaoAndObservationTimePeriod(cmd.icao(), period)
			                          .stream()
			                          .map(UniqueKey::from)
			                          .collect(Collectors.toSet());

		List<Metar> toSaveMetars = toInsertMetars.stream()
			                           .filter(m -> {
				                           UniqueKey uk = UniqueKey.from(m.metar());
				                           if (!seen.add(uk) || existing.contains(uk)) {
					                           duplicates.add(new DuplicatedItem(m.lineNo()));
					                           return false;
				                           }
				                           return true;
			                           })
			                           .map(InsertMetar::metar)
			                           .toList();

		managementOutputPort.saveAll(toSaveMetars);
		return new MetarSaveFileResult(
			toSaveMetars.size(),
			parsingErrors.size(),
			duplicates.size(),
			parsingErrorRate,
			parsingErrors,
			duplicates,
			buildMessage(parsingErrorRate)
		);
	}

	private YearMonth extractYearMonth(String line) {
		Matcher match = OBS_TIME_PATTERN.matcher(line);
		if (match.find()) {
			return YearMonth.parse(match.group(1), DateTimeFormatter.ofPattern("yyyy-MM"));
		}
		throw new BusinessException(400, "MISSING_TIMESTAMP", "Missing timestamp (yyyy-mm-dd HH:mm) at line: " + line);
	}

	private String extractRawText(String line) {
		Matcher match = RAW_TEXT_PATTERN.matcher(line);
		if (match.find()) {
			return match.group();
		}
		throw new BusinessException(400, "MISSING_METAR", "Missing metar raw text at line: " + line);
	}

	private static String buildMessage(double parsingErrorRate) {
		if (parsingErrorRate >= 0.50) return "Warning: parsing error rate >= 50%. Dataset quality is very low.";
		if (parsingErrorRate >= 0.30) return "Caution: parsing error rate >= 30%. Dataset quality is low.";
		if (parsingErrorRate >= 0.01) return "Notice: parsing error rate >= 1%.";
		return "";
	}

	private record InsertMetar(int lineNo, Metar metar){};
}
