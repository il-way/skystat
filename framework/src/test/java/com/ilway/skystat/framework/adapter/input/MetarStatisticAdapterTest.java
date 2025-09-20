package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.port.input.metar.MetarManagementInputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.framework.adapter.input.rest.converter.ComparisonConverter;
import com.ilway.skystat.framework.adapter.input.rest.converter.UnitConverter;
import com.ilway.skystat.framework.adapter.output.file.ResourceFileConfig;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(classes = {ResourceFileConfig.class})
@AutoConfigureMockMvc
@Transactional
public class MetarStatisticAdapterTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;

	@Test
	void getThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                .param("field", "peakwind")
			                .param("comparison", "GTE")
			                .param("threshold", "25")
			                .param("unit", "KT")
			                .param("startDateTime", "2019-01-01T00:00:00Z")
			                .param("endDateTime", "2025-01-01T00:00:00Z")
			                .accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk());


	}

}
