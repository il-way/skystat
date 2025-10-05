package com.ilway.skystat.application.port.output;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.temperature.DailyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;

import java.util.List;

public interface TemperatureStatisticQueryOutputPort {

	List<DailyTemperatureStatDto> findDailyTemperatureStatistic(String icao, RetrievalPeriod period);

	List<HourlyTemperatureStatDto> findHourlyTemperatureStatistic(String icao, RetrievalPeriod period);

}
