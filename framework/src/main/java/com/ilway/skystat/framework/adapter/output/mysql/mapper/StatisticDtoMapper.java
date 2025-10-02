package com.ilway.skystat.framework.adapter.output.mysql.mapper;

import com.ilway.skystat.application.dto.statistic.HourlyCountDto;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.exception.AggregationUnavailableException;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;

import java.util.List;
import java.util.function.Supplier;

public class StatisticDtoMapper {

	public static MonthlyCountDto mapMonthly(MonthlyCountQueryDto c) {
		return new MonthlyCountDto(c.year(), c.month(), c.count());
	}

	public static HourlyCountDto mapHourly(HourlyCountQueryDto c) {
		return new HourlyCountDto(c.year(), c.month(), c.hour(), c.count());
	}

	public static <T> List<T> when(boolean isSatisfiedBy, Supplier<List<T>> body) {
		if (!isSatisfiedBy) throw new AggregationUnavailableException("Query is not supplied.");
		return body.get();
	}

}
