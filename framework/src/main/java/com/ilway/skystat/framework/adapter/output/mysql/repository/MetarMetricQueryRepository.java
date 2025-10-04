package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarMetricQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.windSpeedKt >= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyCountQueryDto> countWindSpeedKtGteByMonth(@Param("icao") String icao,
	                                                      @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                      @Param("toExclusive") ZonedDateTime toExclusive,
	                                                      @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
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
	List<HourlyCountQueryDto> countWindSpeedKtGteByMonthHour(@Param("icao") String icao,
	                                                         @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                         @Param("toExclusive") ZonedDateTime toExclusive,
	                                                         @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.windPeakKt >= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyCountQueryDto> countWindPeakKtGteByMonth(@Param("icao") String icao,
	                                                     @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                     @Param("toExclusive") ZonedDateTime toExclusive,
	                                                     @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.windPeakKt >= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<HourlyCountQueryDto> countWindPeakKtGteByMonthHour(@Param("icao") String icao,
	                                                        @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                        @Param("toExclusive") ZonedDateTime toExclusive,
	                                                        @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.visibilityM <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyCountQueryDto> countVisibilityMeterLteByMonth(@Param("icao") String icao,
	                                                          @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                          @Param("toExclusive") ZonedDateTime toExclusive,
	                                                          @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
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
	List<HourlyCountQueryDto> countVisibilityMeterLteByMonthHour(@Param("icao") String icao,
	                                                             @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                             @Param("toExclusive") ZonedDateTime toExclusive,
	                                                             @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.ceiling <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyCountQueryDto> countCeilingLteByMonth(@Param("icao") String icao,
	                                                  @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                  @Param("toExclusive") ZonedDateTime toExclusive,
	                                                  @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
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
	List<HourlyCountQueryDto> countCeilingLteByMonthHour(@Param("icao") String icao,
	                                                     @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                     @Param("toExclusive") ZonedDateTime toExclusive,
	                                                     @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND m.altimeterHpa <= :threshold
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyCountQueryDto> countAltimeterHpaLteByMonth(@Param("icao") String icao,
	                                                       @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                       @Param("toExclusive") ZonedDateTime toExclusive,
	                                                       @Param("threshold") double threshold);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
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
	List<HourlyCountQueryDto> countAltimeterHpaLteByMonthHour(@Param("icao") String icao,
	                                                          @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                          @Param("toExclusive") ZonedDateTime toExclusive,
	                                                          @Param("threshold") double threshold);

}
