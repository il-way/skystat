package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.AverageSummaryQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountSummaryQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarSummaryQueryRepository extends JpaRepository<MetarData, Long> {

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
	AverageSummaryQueryDto averageSummary(@Param("icao") String icao,
	                                      @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                      @Param("toExclusive") ZonedDateTime toExclusive);


	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountSummaryQueryDto(
		        YEAR(m.reportTime),
		        MONTH(m.reportTime),
		        COUNT(DISTINCT CASE WHEN m.windPeakKt >= :windPeakThreshold THEN DAY(m.reportTime) ELSE NULL END),
		        COUNT(DISTINCT CASE WHEN m.visibilityM <= :visibilityThreshold THEN DAY(m.reportTime) ELSE NULL END),
		        COUNT(DISTINCT CASE WHEN m.ceiling <= :ceilingThreshold THEN DAY(m.reportTime) ELSE NULL END),
		        COUNT(DISTINCT CASE WHEN wp.phenomenon = :phenomenon THEN DAY(m.reportTime) ELSE NULL END),
		        COUNT(DISTINCT CASE WHEN wd.descriptor = :descriptor THEN DAY(m.reportTime) ELSE NULL END)
		  )
		  FROM MetarData m
		  LEFT JOIN m.weathers w
		  LEFT JOIN w.phenomena wp
		  LEFT JOIN w.descriptors wd
		  WHERE m.stationIcao = :icao
		      AND m.reportTime >= :fromInclusive
		      AND m.reportTime < :toExclusive
		  GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		  ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountSummaryQueryDto> countSummaryByMonth(@Param("icao") String icao,
	                                                      @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                      @Param("toExclusive") ZonedDateTime toExclusive,
	                                                      @Param("windPeakThreshold") double windPeakThreshold,
	                                                      @Param("visibilityThreshold") double visibilityThreshold,
	                                                      @Param("ceilingThreshold") double ceilingThreshold,
	                                                      @Param("phenomenon") WeatherPhenomenon phenomenon,
	                                                      @Param("descriptor") WeatherDescriptor descriptor);

}
