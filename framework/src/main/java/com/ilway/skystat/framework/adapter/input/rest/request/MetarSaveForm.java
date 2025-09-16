package com.ilway.skystat.framework.adapter.input.rest.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MetarSaveForm {

	@NotNull
	@NotEmpty
	private String rawText;

	@NotNull
	private ZonedDateTime observationTime;

}
