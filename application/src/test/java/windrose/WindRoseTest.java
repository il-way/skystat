package windrose;

import java.time.Month;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dto.RetrievalPeriod;
import dto.windrose.DirectionBin;
import dto.windrose.SpeedBin;
import dto.windrose.WindRose;
import port.input.WindRoseInputPort;
import usecase.WindRoseUseCase;
import windrose.data.WindRoseTestData;

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
