package com.ilway.skystat.framework.adapter.input.rest.response;

import java.util.List;

public record MetarSaveResponse(int successCount, int parseFailureCount, int duplicatedCount,
                                List<ParsedErrorItem> parsedErrors, List<DuplicatedItem> duplicates,
                                String message) {

	public static MetarSaveResponse success(Integer successCount, List<ParsedErrorItem> parsedErrors, List<DuplicatedItem> duplicates) {
		return new MetarSaveResponse(
			successCount,
			parsedErrors != null ? parsedErrors.size() : 0,
			duplicates != null ? duplicates.size() : 0,
			parsedErrors != null ? parsedErrors : List.of(),
			duplicates != null ? duplicates : List.of(),
			"Metar save success."
		);
	}

	public static MetarSaveResponse failure(int parseFailureCount, int duplicatedCount, String message) {
		return new MetarSaveResponse(0, parseFailureCount, duplicatedCount, List.of(), List.of(), message);
	}

	public record ParsedErrorItem(int lineNo, String rawText, String errorMessage) {}

	public record DuplicatedItem(int lineNo) {}

}
