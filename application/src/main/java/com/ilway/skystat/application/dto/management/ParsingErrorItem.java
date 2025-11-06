package com.ilway.skystat.application.dto.management;

public record ParsingErrorItem(
	int lineNo,
	String rawText,
	String message
) {
}
