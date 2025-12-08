package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.dto.management.MetarSaveFileCommand;
import com.ilway.skystat.application.dto.management.MetarSaveFileResult;
import com.ilway.skystat.application.dto.management.MetarSaveOneCommand;
import com.ilway.skystat.application.port.input.MetarManagementUseCase;
import com.ilway.skystat.application.port.input.MetarSaveFileUseCase;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarFileUploadForm;
import com.ilway.skystat.framework.adapter.input.rest.request.MetarSaveForm;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequestMapping("/api/metar")
@RestController
@RequiredArgsConstructor
@Validated
public class MetarManagementAdapter {

	private final MetarManagementUseCase metarManagementUseCase;
	private final MetarSaveFileUseCase metarSaveFileUseCase;

	@PostMapping("/save/{icao}")
	public ResponseEntity<MetarSaveResponse> save(
		@PathVariable("icao") String icao,
		@RequestBody @Validated MetarSaveForm form
	) {
		MetarSaveOneCommand cmd = new MetarSaveOneCommand(icao, form.getRawText(), form.getObservationTime());
		metarManagementUseCase.save(cmd);

		return ResponseEntity.ok().body(
			MetarSaveResponse.success(1, null, null)
		);
	}

	@PostMapping("/save/upload/{icao}")
	public ResponseEntity<MetarSaveResponse> saveAll(
		@PathVariable("icao") String icao,
		@Validated @ModelAttribute MetarFileUploadForm form
	) {
		try {
			MetarSaveFileCommand cmd = new MetarSaveFileCommand(icao, form.getFile().getOriginalFilename(), form.getFile().getInputStream(), UTF_8);

			MetarSaveFileResult result = metarSaveFileUseCase.saveFile(cmd);

			return ResponseEntity.ok().body(
				MetarSaveResponse.success(
					result.successCount(), result.parsingErrors(), result.duplicates()
				)
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read upload file", e);
		}
	}

}
