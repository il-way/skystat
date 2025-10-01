package com.ilway.skystat.framework.adapter.input.rest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

	private List<String> allowedContentTypes;

	@Override
	public void initialize(ValidFile constraintAnnotation) {
		this.allowedContentTypes = Arrays.asList(
			constraintAnnotation.contentTypes()
		);
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null || file.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Select a file toExclusive upload.")
				.addConstraintViolation();
			return false;
		}

		if (!allowedContentTypes.isEmpty()) {
			String contentType = file.getContentType();
			if (contentType == null || !allowedContentTypes.contains(contentType)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Now allowed content type. " + allowedContentTypes.toString() + " is allowed.")
					.addConstraintViolation();
				return false;
			}
		}

		return true;
	}
}
