package adapter.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import usecase.MetarManagementUseCase;

@RestController
@RequiredArgsConstructor
public class MetarManagementResourceFileAdapter {

	private final MetarManagementUseCase useCase;



}
