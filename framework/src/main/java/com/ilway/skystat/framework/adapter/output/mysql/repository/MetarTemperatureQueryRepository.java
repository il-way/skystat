package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.application.dto.statistic.temperature.DailyTemperatureStatDto;
import com.ilway.skystat.application.dto.statistic.temperature.HourlyTemperatureStatDto;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarTemperatureQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.DailyTemperatureStatDto(
				YEAR(m.reportTime), MONTH(m.reportTime), DAY(m.reportTime),
				AVG(m.temperature),
				MAX(m.temperature),
				MIN(m.temperature)
			)
			FROM MetarData m
			WHERE UPPER(:m.stationIcao) = UPPER(:icao)
				AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), DAY(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), DAY(m.reportTime)
		""")
	List<DailyTemperatureStatDto> findDailyStatistic(@Param("icao") String icao,
	                                                 @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                 @Param("toExclusive") ZonedDateTime toExclusive);


	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.DailyTemperatureStatDto(
				YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
				AVG(m.temperature),
				MAX(m.temperature),
				MIN(m.temperature)
			)
			FROM MetarData m
			WHERE UPPER(:m.stationIcao) = UPPER(:icao)
				AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyTemperatureStatDto> findHourlyStatistic(@Param("icao") String icao,
	                                                   @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                   @Param("toExclusive") ZonedDateTime toExclusive);
}
