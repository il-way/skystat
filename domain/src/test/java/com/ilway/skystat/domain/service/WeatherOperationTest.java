package com.ilway.skystat.domain.service;

import com.ilway.skystat.domain.vo.weather.Weather;
import com.ilway.skystat.domain.vo.weather.Weathers;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescription;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherIntensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.TS;
import static com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor.VC;
import static com.ilway.skystat.domain.vo.weather.type.WeatherIntensity.LIGHT;
import static com.ilway.skystat.domain.vo.weather.type.WeatherIntensity.MODERATE;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherOperationTest {

	@Test
	@DisplayName("""
			검증대상 : 단일 Phenomenon 탐색
			요구사항 : 단일 Phenomena 검증 시 정확히 일치하지 않으면 실패해야 한다.
			예상결과 : 실패
		""")
	void containsPhenomenonTest() {
		List<WeatherPhenomenon> target = List.of(RA);

		Weathers weathers = Weathers.builder()
			                    .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA, SN)))
			                    .weather(Weather.of(LIGHT, List.of(TS), List.of(SN)))
			                    .build();

		assertFalse(WeatherOperation.containsPhenomena(weathers, target));
	}

	@Test
	@DisplayName("""
			검증대상 : 단일 Phenomenon 탐색
			요구사항 : 단일 Phenomena 검증 시 정확히 일치하지 않으면 실패해야 한다.
			예상결과 : 실패
		""")
	void containsPhenomenonTest2() {
		List<WeatherPhenomenon> target = List.of(RA);

		Weathers weathers = Weathers.builder()
			                    .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA)))
			                    .build();

		assertTrue(WeatherOperation.containsPhenomena(weathers, target));
	}

	@Test
	@DisplayName("""
			검증대상 : 복합 Phenomenon 탐색
			요구사항 : 복합 Phenomena 검증 시 정확히 일치하면 성공해야 한다.
			예상결과 : 성공
		""")
	void containsPhenomenaTest() {
		List<WeatherPhenomenon> target = List.of(RA, SN);

		Weathers weathers = Weathers.builder()
			                    .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA, SN, PL)))
			                    .weather(Weather.of(LIGHT, List.of(TS), List.of(SN, RA)))
			                    .build();

		assertFalse(WeatherOperation.containsPhenomena(weathers, target));
	}

	@Test
	@DisplayName("""
			검증대상 : 복합 Phenomenon 탐색
			요구사항 : 복합 Phenomena 검증 시 정확히 일치하면 성공해야 한다.
			예상결과 : 성공
		""")
	void containsPhenomenaTest2() {
		List<WeatherPhenomenon> target = List.of(RA, SN);

		Weathers weathers = Weathers.builder()
			                    .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA, SN)))
			                    .build();

		assertTrue(WeatherOperation.containsPhenomena(weathers, target));
	}


	@Test
	@DisplayName("""
			검증대상 : 복합 Weather 탐색
			요구사항 : Descriptor와 Phenomena를 동시 검증 시 정확히 일치하지 않으면 실패해야 한다.
			예상결과 : 실패
		""")
	void containsDescriptorsAndPhenomenaTest() {
		List<WeatherDescription> target = List.of(VC, TS, RA);

		Weathers weathers = Weathers.builder()
			                 .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA, SN)))
			                 .weather(Weather.of(LIGHT, List.of(TS), List.of(RA)))
			                 .build();

		assertFalse(WeatherOperation.containsDescriptorsAndPhenomena(weathers, target));
	}

	@Test
	@DisplayName("""
			검증대상 : 복합 Weather
			요구사항 : Descriptor와 Phenomena를 동시 검증 시 정확히 일치하지 않으면 실패해야 한다.
			예상결과 : 성공
		""")
	void containsDescriptorsAndPhenomenaTest2() {
		List<WeatherDescription> target = List.of(VC, TS, RA);

		Weathers weathers = Weathers.builder()
			                    .weather(Weather.of(MODERATE, List.of(VC, TS), List.of(RA)))
			                    .build();

		assertTrue(WeatherOperation.containsDescriptorsAndPhenomena(weathers, target));
	}

}
