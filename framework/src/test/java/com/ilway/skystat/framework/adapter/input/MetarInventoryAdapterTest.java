package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.framework.adapter.input.rest.response.DatasetCoverageResponse;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MetarInventoryAdapterTest extends MySQLConfigData {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Value("classpath:data/metar/RKSI.txt")
	Resource rksiMetarFile;

	private static final String TEST_ICAO = "RKSI";

	public MetarInventoryAdapterTest(@Autowired MetarManagementRepository repository,
	                                  @Autowired EntityManager em,
	                                  @Autowired MetarInventoryRepository metarInventoryRepository) {
		super(repository, em, metarInventoryRepository);
	}

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
	@DisplayName("DatasetCoverage 조회에 성공해야 한다.")
	void getDatasetCoverageTest() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/api/metar/dataset")
			                                      .param("icao", icao)
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		DatasetCoverageResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			DatasetCoverageResponse.class
		);

		assertEquals(87568, response.totalCount());
	}

}
