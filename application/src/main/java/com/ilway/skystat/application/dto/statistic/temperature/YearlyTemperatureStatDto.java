package com.ilway.skystat.application.dto.statistic.temperature;

/**
 * 연도별 기온 통계값용 DTO
 *
 * @param year
 * @param dailyMeanAvg 연별 기온 평균. 특정 연도의 일평균 기온을 평균한 값을 말한다
 * @param dailyMaxAvg  연별 최고기온 평균. 특정 연도의 일 최고 기온을 평균한 값을 말한다
 * @param dailyMinAvg  연별 최저기온 평균. 특정 연도의 일 최저 기온을 평균한 값을 말한다
 * @param yearlyMax   연별 최고기온. 특정 연도의 최고 기온을 말한다
 * @param yearlyMin   연별 최저기온. 특정 연도의 최저 기온을 말한다
 */
public record YearlyTemperatureStatDto(
	int year,
	Double dailyMeanAvg,
	Double dailyMaxAvg,
	Double dailyMinAvg,
	Double yearlyMax,
	Double yearlyMin
) {
}
