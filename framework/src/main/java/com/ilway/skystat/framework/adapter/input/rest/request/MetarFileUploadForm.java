package com.ilway.skystat.framework.adapter.input.rest.request;

import com.ilway.skystat.framework.adapter.input.rest.validator.ValidFile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MetarFileUploadForm {

	@NotNull
	@ValidFile(contentTypes = {"text/plain"})
	private MultipartFile file;

	private String description;

}
