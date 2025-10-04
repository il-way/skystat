package com.ilway.skystat.application.dto.statistic.temperature;

public record MonthlyTemperatureAvgAcrossYearsDto(
	int month,
	int yearCount,
	Double dailyMeanAvgOfYears,    // (각 연-월의 dailyMeanAvg)들의 평균
	Double dailyMaxAvgOfYears,     // (각 연-월의 dailyMaxAvg)들의 평균
	Double dailyMinAvgOfYears,     // (각 연-월의 dailyMinAvg)들의 평균
	Double monthlyMaxAvgOfYears,   // (각 연-월의 monthlyMax)들의 평균
	Double monthlyMinAvgOfYears    // (각 연-월의 monthlyMin)들의 평균
) {
}
