package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.framework.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MetarManagementAdapterTest extends MySQLConfigData {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;

	public MetarManagementAdapterTest(@Autowired MetarManagementRepository repository,
	                                  @Autowired EntityManager em,
	                                  @Autowired MetarInventoryRepository metarInventoryRepository) {
		super(repository, em, metarInventoryRepository);
	}

	@Value("classpath:data/metar/RKSI.txt")
	Resource rksiMetarFile;

	private static final String TEST_ICAO = "RKSI";
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Test
	@DisplayName("파입 업로드를 통해 여러 METAR 저장에 성공해야 한다 (multipart/form-data)")
	void saveAllTest() throws Exception {
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

		MetarSaveResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			MetarSaveResponse.class
		);

		Path path = Path.of(rksiMetarFile.getURI());
		int expectedTotalCount = Files.readAllLines(path).size();
		int expectedSuccessCount = response.successCount();
		int expectedFailureCount = response.parseFailureCount();
		int expectedDuplicatedCount = response.duplicatedCount();

		List<Metar> actual = metarManagementUseCase.findAllByIcao(TEST_ICAO);
		int actualSuccessCount = actual.size();

		assertAll(
			() -> assertEquals(expectedSuccessCount, actualSuccessCount),
			() -> assertEquals(expectedTotalCount - expectedFailureCount, actualSuccessCount)
		);

	}

	@Test
	void saveTestSuccess() throws Exception {
		String icao = "RKSI";
		String rawText = "RKSI 010000Z 07001KT 800 TS -RASN FZFG FEW008 FEW100 FEW150 SCT200 SCT250 M06/M11 Q1034 NOSIG";
		var body = Map.of(
			"observationTime", "2019-01-01T00:00:00Z",
			"rawText", rawText
		);

		mockMvc.perform(post("/api/metar/save/{icao}", icao)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(body))
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.successCount").value(1))
			.andExpect(jsonPath("$.parseFailureCount").value(0))
			.andExpect(jsonPath("$.duplicatedCount").value(0));


		List<Metar> list = metarManagementUseCase.findAllByIcao(icao);
		Weathers weathers = list.getFirst().getWeathers();
		Clouds clouds = list.getFirst().getClouds();

		assertEquals(rawText, list.getFirst().getRawText());
	}

	@Test
	void saveTestFailure() throws Exception {
		String icao = "RKSI";
		String rawText = "RKSI ????";
		var body = Map.of(
			"observationTime", "2019-01-01T00:00:00Z",
			"rawText", rawText
		);

		mockMvc.perform(post("/api/metar/save/{icao}", icao)
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(body))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.successCount").value(0))
			.andExpect(jsonPath("$.parseFailureCount").value(1))
			.andExpect(jsonPath("$.duplicatedCount").value(0));

		List<Metar> list = metarManagementUseCase.findAllByIcao(icao);
		assertEquals(0, list.size());
	}


}
