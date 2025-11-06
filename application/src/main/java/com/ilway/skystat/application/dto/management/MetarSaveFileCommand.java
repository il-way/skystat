package com.ilway.skystat.application.dto.management;

import java.io.InputStream;
import java.nio.charset.Charset;

public record MetarSaveFileCommand(
	String icao,
	String fileName,
	InputStream content,
	Charset charset
) {
}
