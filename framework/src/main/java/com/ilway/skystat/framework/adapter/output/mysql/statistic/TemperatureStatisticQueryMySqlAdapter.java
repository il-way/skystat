package com.ilway.skystat.framework.adapter.output.mysql.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.DailyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;
import com.ilway.skystat.application.port.output.TemperatureStatisticQueryOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarTemperatureQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TemperatureStatisticQueryMySqlAdapter implements TemperatureStatisticQueryOutputPort {

	private final MetarTemperatureQueryRepository repository;

	@Override
	public List<DailyTemperatureStatDto> findDailyTemperatureStatistic(String icao, RetrievalPeriod period) {
		return repository.findDailyStatistic(icao, period.fromInclusive(), period.toExclusive());
	}

	@Override
	public List<HourlyTemperatureStatDto> findHourlyTemperatureStatistic(String icao, RetrievalPeriod period) {
		return repository.findHourlyStatistic(icao, period.fromInclusive(), period.toExclusive());
	}
}
