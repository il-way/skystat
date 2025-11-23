package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.framework.adapter.input.rest.response.WindRoseResponse;
import com.ilway.skystat.framework.config.ResourceFileConfigData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WindRoseAdapterTest extends ResourceFileConfigData {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Value("classpath:data/metar/RKSI.txt")
	Resource rksiMetarFile;

	private static final String TEST_ICAO = "RKSI";

	@BeforeEach
	void saveAll() throws Exception {
		MockMultipartFile mockFile = new MockMultipartFile(
			"file",
			rksiMetarFile.getFilename(),
			"text/plain",
			rksiMetarFile.getInputStream()
		);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/metar/save/upload/{icao}", TEST_ICAO)
			                                      .file(mockFile)
			                                      .param("description", "Test with a real resource file")
			                                      .contentType(MediaType.MULTIPART_FORM_DATA))
			                      .andExpect(status().isOk())
			                      .andReturn();
	}

	@Test
	@DisplayName("바람장미(windrose) 조회에 성공해야 한다")
	void getDefaultWindRoseTest() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/api/metar/windrose")
			                                      .param("icao", icao)
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z"))
			                      .andExpect(status().isOk())
			                      .andReturn();

		WindRoseResponse windRoseResponse = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			WindRoseResponse.class
		);

		log.info("# windrose total count : {}", windRoseResponse.totalCount());
		log.info("# windrose sample size : {}", windRoseResponse.sampleSize());
		log.info("# windrose missing count : {}", windRoseResponse.variableSize());
		log.info("# windrose data : {}", windRoseResponse.data());

	}


}
