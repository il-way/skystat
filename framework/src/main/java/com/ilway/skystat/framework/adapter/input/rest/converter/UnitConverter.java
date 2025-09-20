package com.ilway.skystat.framework.adapter.input.rest.converter;

import com.ilway.skystat.domain.vo.unit.LengthUnit;
import com.ilway.skystat.domain.vo.unit.PressureUnit;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;
import com.ilway.skystat.domain.vo.unit.Unit;
import org.springframework.core.convert.converter.Converter;

public class UnitConverter implements Converter<String, Unit> {
	@Override
	public Unit convert(String source) {
		if (source == null) return null;
		String s = source.trim().toUpperCase();
		for (SpeedUnit u : SpeedUnit.values()) {
			if (u.name().equals(s) || u.getSymbol().equalsIgnoreCase(s)) return u;
		}

		for (LengthUnit u : LengthUnit.values()) {
			if (u.name().equals(s)) return u;
		}

		for (PressureUnit u : PressureUnit.values()) {
			if (u.name().equals(s)) return u;
		}

		throw new IllegalArgumentException("Unsupported unit: " + source);
	}
}
