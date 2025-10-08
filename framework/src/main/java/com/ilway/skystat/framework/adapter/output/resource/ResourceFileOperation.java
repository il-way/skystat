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

	public record MetarRow(int lineNo, String icao, ZonedDateTime time, String rawText){}

	public record ParseError(String rawText, String errorMessage) {}

	public record ParseResult(int lineNo, Metar metar, ParseError error) {

		public boolean isSuccess() {
			return metar != null;
		}

		public static ParseResult success(int lineNo, Metar metar) {
			return new ParseResult(lineNo, metar, null);
		}

		public static ParseResult failure(int lineNo, String rawText, Exception e) {
			return new ParseResult(lineNo, null, new ParseError(rawText, e.getMessage()));
		}
	}

	public static MetarRow parseUploadFile(int lineNo, String line, DateTimeFormatter FMT) {
		String[] parts = line.split(",", 3);
		String icao = parts[0].trim();
		String ts = parts[1].trim();
		String raw = parts[2].trim();

		try {
			ZonedDateTime time = LocalDateTime.parse(ts, FMT).atZone(UTC);
			return new MetarRow(lineNo, icao, time, raw);
		} catch (Exception e) {
			return null;
		}
	}

	public static ParseResult parseMetarSafely(MetarRow r, MetarParser parser) {
		try {
			parser.setYearMonth(YearMonth.from(r.time()));
			Metar parsedMetar = parser.parse(r.rawText());

			if (parsedMetar == null) {
				return ParseResult.failure(r.lineNo(), r.rawText(), new RuntimeException("Parser return null"));
			}
			return ParseResult.success(r.lineNo(), parsedMetar);
		} catch (Exception e) {
			return ParseResult.failure(r.lineNo(), r.rawText(), e);
		}
	}
}
