package com.ilway.skystat.application.dto.management;

import java.util.List;

public record MetarSaveFileResult(
	int successCount,
	int parseFailureCount,
	int duplicatedCount,
	double parsingErrorRate,
	List<ParsingErrorItem> parsingErrors,
	List<DuplicatedItem> duplicates,
	String message
) {

	public MetarSaveFileResult(int successCount,
	                           int parseFailureCount,
	                           int duplicatedCount,
	                           double parsingErrorRate,
	                           List<ParsingErrorItem> parsingErrors,
	                           List<DuplicatedItem> duplicates) {
		this(successCount, parseFailureCount, duplicatedCount, parsingErrorRate, parsingErrors, duplicates, "");
	}

}
