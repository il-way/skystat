package com.ilway.skystat.framework.adapter.input.rest.response;

import com.ilway.skystat.framework.adapter.output.resource.ResourceFileOperation.ParseError;
import lombok.AllArgsConstructor;

import java.util.List;

public record MetarSaveResponse(Integer successCount, Integer failureCount, List<ParseError> errorList,
                                String message) {

	public static MetarSaveResponse success(Integer successCount, Integer failureCount, List<ParseError> errorList) {
		return new MetarSaveResponse(successCount, failureCount, errorList, "Metar save success.");
	}

	public static MetarSaveResponse failure(String message) {
		return new MetarSaveResponse(0, 1, List.of(), message);
	}

}
