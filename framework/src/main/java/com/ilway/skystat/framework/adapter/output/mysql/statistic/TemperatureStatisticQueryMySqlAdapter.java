package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.DailyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;
import com.ilway.skystat.application.port.output.TemperatureStatisticQueryOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarTemperatureQueryRepository;
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

@TranslateDbExceptions("querying temperature statistic")
@RequiredArgsConstructor
public class TemperatureStatisticQueryMySqlAdapter implements TemperatureStatisticQueryOutputPort {

	private final MetarTemperatureQueryRepository repository;

	@Override
	public List<DailyTemperatureStatDto> findDailyTemperatureStatistic(@UppercaseParam String icao, RetrievalPeriod period) {
		return repository.findDailyStatistic(icao, period.fromInclusive(), period.toExclusive());
	}

	@Override
	public List<HourlyTemperatureStatDto> findHourlyTemperatureStatistic(@UppercaseParam String icao, RetrievalPeriod period) {
		return repository.findHourlyStatistic(icao, period.fromInclusive(), period.toExclusive());
	}
}
