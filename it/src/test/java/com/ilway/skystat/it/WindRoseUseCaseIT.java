package com.ilway.skystat.it;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.application.port.input.WindRoseInputPort;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.service.TimeOperation;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.resource.MetarManagementResourceFileAdapter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import com.ilway.skystat.application.usecase.WindRoseUseCase;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ilway.skystat.domain.service.TimeOperation.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ItTestApp.class)
@RequiredArgsConstructor
public class WindRoseUseCaseIT {

	@Autowired WindRoseUseCase windRoseUseCase;
	@Autowired MetarManagementResourceFileAdapter outputPort;
	@Autowired ApplicationContext ctx;


	@Test
	void WindRoseUseCase_의존성_주입에_성공해야한다() {
		String actual = windRoseUseCase.getClass().getSimpleName();
		String expected = WindRoseInputPort.class.getSimpleName();
		assertEquals(expected, actual);
	}

	@Test
	void MonthlyWindRoseMap_생성에_성공해야한다() {
		String icao = "rksi";
		RetrievalPeriod period = new RetrievalPeriod(
			ofLenientUtc(2019, 1, 1, 0, 0),
			ofLenientUtc(2020, 1, 1, 0, 0)
		);
		List<SpeedBin> speedBins = SpeedBin.of5KtSpeedBins();
		List<DirectionBin> directionBins = DirectionBin.of16DirectionBins();

		Map<Month, WindRose> windRoseMap = windRoseUseCase.generateMonthlyWindRose(icao, period, speedBins, directionBins);

		assertAll(
			() -> assertEquals(12, windRoseMap.size())
		);

	}



}
