package com.ilway.skystat.domain.spec;

import com.ilway.skystat.domain.exception.GenericSpecificationExeception;
import com.ilway.skystat.domain.service.WindOperation;
import com.ilway.skystat.domain.spec.shared.AbstractSpecification;
import com.ilway.skystat.domain.vo.weather.Wind;

import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;

public class WindSpeedSpec extends AbstractSpecification<Wind> {

	@Override
	public boolean isSatisfiedBy(Wind wind) {
		double windPeak = WindOperation.getWindPeak(wind);
		return wind.getUnit().convertTo(windPeak, KT) <= 100;
	}

	@Override
	public void check(Wind wind) throws GenericSpecificationExeception {
		if (!isSatisfiedBy(wind)) {
			throw new GenericSpecificationExeception("Wind speed can't exceed 100KT, but wind speed: "
				                                         + wind.getSpeed()
				                                         + ", wind gusts: "
				                                         + wind.getGusts()
				                                         + ", wind unit: " + wind.getUnit());
		}
	}
}
