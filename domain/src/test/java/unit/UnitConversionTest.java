package unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vo.unit.LengthUnit;
import vo.unit.SpeedUnit;

import static vo.unit.LengthUnit.FEET;
import static vo.unit.LengthUnit.METERS;
import static vo.unit.SpeedUnit.KT;

public class UnitConversionTest {

	@Test
	void 속도단위_변환에_성공해야_한다() {
		double actual = SpeedUnit.MPS.convertTo(10, KT);
		assertEquals(19.4384, actual);
	}

	@Test
	void 길이단위_변환에_성공해야한다() {
		double actual = LengthUnit.MILE.convertTo(1, METERS);
		assertTrue(actual <= 1620);
	}

}
