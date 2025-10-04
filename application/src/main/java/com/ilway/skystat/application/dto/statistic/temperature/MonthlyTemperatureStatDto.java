package com.ilway.skystat.application.dto.statistic.temperature;

/**
 * 연월별 기온 통계값용 DTO
 *
 * @param year
 * @param month
 * @param dailyMeanAvg 연월별 기온 평균. 특정 연도, 특정 월의 일평균 기온을 평균한 값을 말한다
 * @param dailyMaxAvg  연월별 평균 최고기온. 특정 연도, 특정 월에서 매일 관측된 최고 기온을 평균한 값을 말한다
 * @param monthlyMax   연월별 최고기온. 특정 연도, 특정 월에서 관측된 최고기온을 말한다
 * @param monthlyMin   연월별 최저기온. 특정 연도, 특정 월에서 관측된 최저기온을 말한다
 */
public record MonthlyTemperatureStatDto(
	int year,
	int month,
	Double dailyMeanAvg,
	Double dailyMaxAvg,
	Double dailyMinAvg,
	Double monthlyMax,
	Double monthlyMin
) {
}
