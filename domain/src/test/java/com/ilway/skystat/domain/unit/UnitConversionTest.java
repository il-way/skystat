package com.ilway.skystat.domain.unit;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.FEET;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.MPS;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.CELSIUS;
import static com.ilway.skystat.domain.vo.unit.TemperatureUnit.FAHRENHEIT;
import static org.junit.jupiter.api.Assertions.*;

import com.ilway.skystat.domain.vo.unit.TemperatureUnit;
import org.junit.jupiter.api.Test;
import com.ilway.skystat.domain.vo.unit.LengthUnit;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;

import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;

public class UnitConversionTest {

	@Test
	void 속도단위_변환에_성공해야_한다() {
		double actual = SpeedUnit.MPS.convertTo(10, KT);
		assertEquals(19.4384, actual, 0.001);
	}

	@Test
	void 길이단위_변환에_성공해야한다() {
		double actual = LengthUnit.MILE.convertTo(1, METERS);
		assertTrue(actual <= 1620);
	}

	@Test void meter_to_feet() {
		double ft = LengthUnit.METERS.convertTo(1, FEET); // 1 m → ft
		assertEquals(3.28084, ft, 1e-5);
	}

	@Test void feet_to_meter() {
		double m = LengthUnit.FEET.convertTo(3.28084, METERS); // 3.28084 ft → m
		assertEquals(1.0, m, 1e-5);
	}

	@Test void c_to_f() {
		double f = TemperatureUnit.CELSIUS.convertTo(10, FAHRENHEIT); // 3.28084 ft → m
		assertEquals(50, f, 1e-5);
	}

	@Test void f_to_c() {
		double c = FAHRENHEIT.convertTo(50, CELSIUS); // 3.28084 ft → m
		assertEquals(10, c, 1e-5);
	}

}
