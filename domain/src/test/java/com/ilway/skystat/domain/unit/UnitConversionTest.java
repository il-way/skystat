package com.ilway.skystat.domain.unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.ilway.skystat.domain.vo.unit.LengthUnit;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;

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
