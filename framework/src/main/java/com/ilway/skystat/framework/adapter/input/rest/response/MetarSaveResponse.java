package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.application.dto.management.DuplicatedItem;
import com.ilway.skystat.application.dto.management.ParsingErrorItem;
import org.springframework.util.StringUtils;

import java.util.List;

public record MetarSaveResponse(int successCount, int parseFailureCount, int duplicatedCount, double parsingErrorRate,
                                List<ParsingErrorItem> parsingErrors, List<DuplicatedItem> duplicates,
                                String message) {

	public static MetarSaveResponse success(Integer successCount, Double parsingErrorRate, List<ParsingErrorItem> parsingErrors, List<DuplicatedItem> duplicates) {
		return success(successCount, parsingErrorRate, parsingErrors, duplicates, null);
	}

	public static MetarSaveResponse success(Integer successCount, Double parsingErrorRate, List<ParsingErrorItem> parsingErrors, List<DuplicatedItem> duplicates, String message) {
		return new MetarSaveResponse(
			successCount,
			parsingErrors != null ? parsingErrors.size() : 0,
			duplicates != null ? duplicates.size() : 0,
			parsingErrorRate,
			parsingErrors != null ? parsingErrors : List.of(),
			duplicates != null ? duplicates : List.of(),
			StringUtils.hasText(message) ? message : "Metar save success."
		);
	}

	public static MetarSaveResponse failure(Integer parseFailureCount, Integer duplicatedCount, Double parsingErrorRate, String message) {
		return new MetarSaveResponse(0, parseFailureCount, duplicatedCount, parsingErrorRate, List.of(), List.of(), message);
	}

}
