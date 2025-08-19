package com.ilway.skystat.application.windrose;

import java.time.Month;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.windrose.DirectionBin;
import com.ilway.skystat.application.dto.windrose.SpeedBin;
import com.ilway.skystat.application.dto.windrose.WindRose;
import com.ilway.skystat.application.port.input.WindRoseInputPort;
import com.ilway.skystat.application.usecase.WindRoseUseCase;
import com.ilway.skystat.application.windrose.data.WindRoseTestData;

import static org.junit.jupiter.api.Assertions.*;

public class WindRoseTest extends WindRoseTestData {

	private WindRoseUseCase useCase = new WindRoseInputPort(metarManagementOutputPort);

	@Test
	void 바람장미_생산에_성공해야한다() {
		List<SpeedBin> speedBins = SpeedBin.of5KtSpeedBins();
		List<DirectionBin> directionBins = DirectionBin.of16DirectionBins();

		Map<Month, WindRose> windRoseMap = useCase.generateMonthlyWindRose(
			"RKSI", 
			new RetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2025, 1, 1, 0, 0)
			),
			speedBins, 
			directionBins);

		WindRose jan = windRoseMap.get(Month.JANUARY);

		WindRose.BinPair binPair07002KT = new WindRose.BinPair(speedBins.get(1), directionBins.get(3));
		WindRose.BinPair binPair10004KT = new WindRose.BinPair(speedBins.get(1), directionBins.get(4));
		WindRose.BinPair binPair10003KT = new WindRose.BinPair(speedBins.get(1), directionBins.get(4));
		WindRose.BinPair binPair12004KT = new WindRose.BinPair(speedBins.get(1), directionBins.get(5));
		WindRose.BinPair binPair14005KT = new WindRose.BinPair(speedBins.get(1), directionBins.get(6));

		long expectedCount = jan.getTotalCount();
		long actualCount = 5L;

		double expectedRate = jan.getRate(speedBins.get(1), directionBins.get(4));
		double actualRate = 0.4 * 100L;

		assertAll(
			() -> assertEquals(actualCount, expectedCount),
			() -> assertEquals(actualRate, expectedRate)
		);
	}

}
