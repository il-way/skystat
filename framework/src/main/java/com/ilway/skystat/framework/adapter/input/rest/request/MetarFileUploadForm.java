package com.ilway.skystat.framework.adapter.input.rest.request;

import com.ilway.skystat.framework.adapter.input.rest.validator.ValidFile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Data
public class MetarFileUploadForm {

	@NotNull
	@ValidFile(contentTypes = {TEXT_PLAIN_VALUE})
	private MultipartFile file;

	private String description;

}
