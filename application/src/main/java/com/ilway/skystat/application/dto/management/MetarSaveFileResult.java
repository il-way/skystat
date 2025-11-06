package com.ilway.skystat.application.dto.management;

import java.util.List;

public record MetarSaveFileResult(
	int successCount,
	int parseFailureCount,
	int duplicatedCount,
	List<ParsingErrorItem> parsingErrors,
	List<DuplicatedItem> duplicates
) {
}
