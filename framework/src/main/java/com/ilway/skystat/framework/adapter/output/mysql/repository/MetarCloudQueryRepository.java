package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.framework.adapter.output.mysql.data.CloudData;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarCloudQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
    FROM MetarData m
    WHERE UPPER(m.stationIcao) = UPPER(:icao)
      AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
      AND EXISTS (
        SELECT 1 FROM CloudData c
        WHERE c.metar = m AND c.cloudType = :cloudType
      )
    GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
    ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countCloudTypeDaysByMonth(@Param("icao") String icao,
	                                                @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                @Param("toExclusive") ZonedDateTime toExclusive,
	                                                @Param("type") CloudType cloudType);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND EXISTS (
				SELECT 1 FROM CloudData c
				WHERE c.metar = m AND c.cloudType = :cloudType
			)
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countCloudTypeDaysByMonthHour(@Param("icao") String icao,
	                                                          @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                          @Param("toExclusive") ZonedDateTime toExclusive,
	                                                          @Param("type") CloudType cloudType);

}
