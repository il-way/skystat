package com.ilway.skystat.application.dto.statistic;

import java.util.List;

public record ObservationStatisticResult(
	List<MonthlyCountDto> monthly,
	List<HourlyCountDto>  hourly
) { }
