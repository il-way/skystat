package com.ilway.skystat.framework.adapter.input.rest;

import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/metar")
@RestController
@RequiredArgsConstructor
public class MetarManagementAdapter {

	private final MetarManagementUseCase metarManagementUseCase;



}
