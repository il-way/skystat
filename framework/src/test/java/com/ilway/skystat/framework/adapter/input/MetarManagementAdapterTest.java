package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.application.usecase.MetarManagementUseCase;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.FrameworkTestApp;
import com.ilway.skystat.framework.adapter.input.rest.MetarManagementAdapter;
import com.ilway.skystat.framework.adapter.input.rest.response.MetarSaveResponse;
import com.ilway.skystat.framework.parser.metar.MetarParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(classes = {CommonConfig.class})
@AutoConfigureMockMvc
@Transactional
public class MetarManagementAdapterTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired MetarManagementUseCase metarManagementUseCase;

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

		MvcResult mvcResult = mockMvc.perform(multipart("/metar/save/upload/{icao}", TEST_ICAO)
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
		Integer expectedSuccessCount = response.successCount();
		Integer expectedFailureCount = response.failureCount();

		List<Metar> actual = metarManagementUseCase.findAllByIcao(TEST_ICAO);
		int actualSuccessCount = actual.size();

		assertAll(
			() -> assertEquals(expectedSuccessCount, actualSuccessCount),
			() -> assertEquals(expectedTotalCount - expectedFailureCount, actualSuccessCount)
		);

		log.info("# expectedTotalCount: {}", expectedTotalCount);
		log.info("# errorList: {}", response.errorList());
	}


}
