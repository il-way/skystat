package unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vo.unit.SpeedUnit;

import static vo.unit.SpeedUnit.KT;

public class UnitConversionTest {

	@Test
	void 속도단위_변환에_성공해야_한다() {
		double actual = SpeedUnit.MPS.convertTo(10, KT);
		assertEquals(19.4384, actual);
	}

}
