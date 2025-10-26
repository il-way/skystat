package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResult;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.MonthlyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.TemperatureStatisticResult;
import com.ilway.skystat.application.dto.statistic.temperature.YearlyTemperatureStatDto;
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

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MetarStatisticAdapterTest extends ResourceFileConfigData {

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

		MvcResult mvcResult = mockMvc.perform(multipart("/metar/save/upload/{icao}", TEST_ICAO)
			                                      .file(mockFile)
			                                      .param("description", "Test with a real resource file")
			                                      .contentType(MediaType.MULTIPART_FORM_DATA))
			                      .andExpect(status().isOk())
			                      .andReturn();
	}

	@Test
	@DisplayName("peakwind 조건 조회에 성공해야 한다.")
	void getPeakWindThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold")
			                                      .param("icao", icao)
			                                      .param("field", "windpeak")
			                                      .param("comparison", "GTE")
			                                      .param("threshold", "25")
			                                      .param("unit", "KT")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2025-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);

	}

	@Test
	@DisplayName("lowest ceiling 조건 조회에 성공해야 한다.")
	void getCeilingThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold")
			                                      .param("icao", icao)
			                                      .param("field", "ceiling")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "500")
			                                      .param("unit", "feet")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("wind speed 조건 조회에 성공해야 한다.")
	void getWindSpeedThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold")
			                                      .param("icao", icao)
			                                      .param("field", "windspeed")
			                                      .param("comparison", "gte")
			                                      .param("threshold", "15")
			                                      .param("unit", "kt")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("visibility 조건 조회에 성공해야 한다.")
	void getVisisbilityThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold")
			                                      .param("icao", icao)
			                                      .param("field", "visibility")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "800")
			                                      .param("unit", "meters")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("altimeter 조건 조회에 성공해야 한다.")
	void getAltimeterThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold")
			                                      .param("icao", icao)
			                                      .param("field", "altimeter")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "30")
			                                      .param("unit", "inhg")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("cloud type 조회에 성공해야 한다.")
	void getCloudTypeStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/cloud")
			                                      .param("icao", icao)
			                                      .param("condition", "type")
			                                      .param("target", "CB")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("cloud coverage 조회에 성공해야 한다.")
	void getCloudCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/cloud")
			                                      .param("icao", icao)
			                                      .param("condition", "coverage")
			                                      .param("target", "bkn")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("weather 조회에 성공해야 한다.")
	void getWeatherBothCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather")
			                                      .param("icao", icao)
			                                      .param("condition", "both")
			                                      .param("list", "TS,RA")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("weather descriptor 조회에 성공해야 한다.")
	void getWeatherDescriptorCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather")
			                                      .param("icao", icao)
			                                      .param("condition", "descriptor")
			                                      .param("list", "TS")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("weather phenomena 조회에 성공해야 한다.")
	void getWeatherPhenomenaCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather")
			                                      .param("icao", icao)
			                                      .param("condition", "phenomena")
			                                      .param("list", "SN,RA")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResult.class
		);
	}

	@Test
	@DisplayName("temperature 조회에 성공해야 한다.")
	void getTemperatureStatisticTest() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/temperature")
			                                      .param("icao", icao)
			                                      .param("startYear", "2019")
			                                      .param("endYear", "2024")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		TemperatureStatisticResult response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			TemperatureStatisticResult.class
		);

		log.info("# result > {}", response);
	}

}
