package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarMetricQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.windSpeedKt >= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countWindSpeedKtGteByMonth(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.windSpeedKt >= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countWindSpeedKtGteByMonthHour(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.visibilityM <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countVisibilityMeterLteByMonth(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.visibilityM <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countVisibilityMeterLteByMonthHour(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.ceiling <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countCeilingLteByMonth(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.ceiling <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countCeilingLteByMonthHour(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.altimeterHpa <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countAltimeterHpaLteByMonth(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.altimeterHpa <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countAltimeterHpaLteByMonthHour(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, double threshold);

}
