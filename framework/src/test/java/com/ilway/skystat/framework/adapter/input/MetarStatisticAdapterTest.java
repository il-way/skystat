package com.ilway.skystat.framework.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.framework.adapter.output.file.ResourceFileConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = {ResourceFileConfig.class})
@AutoConfigureMockMvc
@Transactional
public class MetarStatisticAdapterTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("peakwind 조건 조회에 성공해야 한다.")
	void getPeakWindThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                                      .param("field", "peakwind")
			                                      .param("comparison", "GTE")
			                                      .param("threshold", "25")
			                                      .param("unit", "KT")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2025-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("lowest ceiling 조건 조회에 성공해야 한다.")
	void getCeilingThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                                      .param("field", "ceiling")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "500")
			                                      .param("unit", "feet")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("wind speed 조건 조회에 성공해야 한다.")
	void getWindSpeedThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                                      .param("field", "windspeed")
			                                      .param("comparison", "gte")
			                                      .param("threshold", "15")
			                                      .param("unit", "kt")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("visibility 조건 조회에 성공해야 한다.")
	void getVisisbilityThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                                      .param("field", "visibility")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "800")
			                                      .param("unit", "meters")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("altimeter 조건 조회에 성공해야 한다.")
	void getAltimeterThresholdStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/threshold/{icao}", icao)
			                                      .param("field", "altimeter")
			                                      .param("comparison", "lte")
			                                      .param("threshold", "30")
			                                      .param("unit", "inhg")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("cloud type 조회에 성공해야 한다.")
	void getCloudTypeStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/cloud/{icao}", icao)
			                                      .param("condition", "type")
			                                      .param("target", "CB")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("cloud coverage 조회에 성공해야 한다.")
	void getCloudCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/cloud/{icao}", icao)
			                                      .param("condition", "coverage")
			                                      .param("target", "bkn")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("weather 조회에 성공해야 한다.")
	void getWeatherBothCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather/{icao}", icao)
			                                      .param("condition", "both")
			                                      .param("list", "TS,RA")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("weather descriptor 조회에 성공해야 한다.")
	void getWeatherDescriptorCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather/{icao}", icao)
			                                      .param("condition", "descriptor")
			                                      .param("list", "TS")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

	@Test
	@DisplayName("weather phenomena 조회에 성공해야 한다.")
	void getWeatherPhenomenaCoverageStatisticTestSuccess() throws Exception {
		String icao = "RKSI";
		MvcResult mvcResult = mockMvc.perform(get("/metar/statistic/weather/{icao}", icao)
			                                      .param("condition", "phenomena")
			                                      .param("list", "SN,RA")
			                                      .param("startDateTime", "2019-01-01T00:00:00Z")
			                                      .param("endDateTime", "2024-01-01T00:00:00Z")
			                                      .accept(MediaType.APPLICATION_JSON))
			                      .andExpect(status().isOk())
			                      .andReturn();

		ObservationStatisticResponse response = objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(UTF_8),
			ObservationStatisticResponse.class
		);
	}

}
