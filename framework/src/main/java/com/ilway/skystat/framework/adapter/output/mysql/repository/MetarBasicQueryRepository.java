package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyAverageQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarBasicQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
			SELECT AVG(m.visibilityM)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	Double averageVisibilityM(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
			SELECT AVG(m.windSpeedKt)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	Double averageWindSpeedKt(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
			SELECT AVG(m.windPeakKt)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	Double averageWindPeakKt(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
			SELECT AVG(m.altimeterHpa)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	Double averageAltimeterHpa(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
			SELECT AVG(m.ceiling)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	Double averageCeiling(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto(
				AVG(m.visibilityM),
				AVG(m.windSpeedKt),
				AVG(m.windPeakKt),
				AVG(m.altimeterHpa),
				AVG(m.ceiling)
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
			ORDER BY m.reportTime
		""")
	AverageSummaryQueryDto averageSummary(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyAverageQueryDto(
			MONTH(m.reportTime), AVG(m.windSpeedKt)
		)
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive
			AND m.reportTime < :toExclusive
		GROUP BY MONTH(m.reportTime)
		ORDER BY MONTH(m.reportTime)
	""")
	List<MonthlyAverageQueryDto> averageWindSpeedKtMonthly(@Param("icao") String icao,
						                                            @Param("fromInclusive") ZonedDateTime fromInclusive,
						                                            @Param("toExclusive") ZonedDateTime toExclusive);


}
