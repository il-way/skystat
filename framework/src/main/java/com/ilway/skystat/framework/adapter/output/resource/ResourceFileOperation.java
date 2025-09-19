package com.ilway.skystat.framework.adapter.output.resource;

import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.input.rest.MetarManagementAdapter;
import com.ilway.skystat.framework.exception.MetarParseException;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import org.hibernate.query.sqm.ParsingException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;

public class ResourceFileOperation {

	public record MetarRow(String icao, ZonedDateTime time, String rawText){}

	public record ParseError(String rawText, String errorMessage) {}

	public record ParseResult(Metar metar, ParseError error) {

		public boolean isSuccess() {
			return metar != null;
		}

		public static ParseResult success(Metar metar) {
			return new ParseResult(metar, null);
		}

		public static ParseResult failure(String rawText, Exception e) {
			return new ParseResult(null, new ParseError(rawText, e.getMessage()));
		}

	}

	public static MetarRow parseUploadFile(String line, DateTimeFormatter FMT) {
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

	public static ParseResult parseMetarSafely(MetarRow r, MetarParser parser) {
		try {
			parser.setYearMonth(YearMonth.from(r.time()));
			Metar parsedMetar = parser.parse(r.rawText());

			if (parsedMetar == null) {
				return ParseResult.failure(r.rawText(), new RuntimeException("Parser return null"));
			}
			return ParseResult.success(parsedMetar);
		} catch (Exception e) {
			return ParseResult.failure(r.rawText(), e);
		}
	}
}
