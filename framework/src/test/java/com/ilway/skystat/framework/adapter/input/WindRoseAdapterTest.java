package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.framework.adapter.output.file.ResourceFileConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = {ResourceFileConfig.class})
@AutoConfigureMockMvc
@Transactional
public class WindRoseAdapterTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("바람장미(windrose) 조회에 성공해야 한다")
	void getWindRoseTest() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/windrose/{}", icao)
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z"))
			                      .andExpect(status().isOk())
			                      .andReturn();

		objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),

		);

		log.info("# wind-rose result: {}" + );

	}


}
