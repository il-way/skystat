package com.ilway.skystat.application.dto.statistic;

import java.time.YearMonth;

public record MonthlyCountDto(
	YearMonth yearMonth,
	long      dyas
) {}
