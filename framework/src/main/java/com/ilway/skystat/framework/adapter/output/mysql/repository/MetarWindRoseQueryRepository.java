package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.WindRoseRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarWindRoseQueryRepository extends JpaRepository<MetarData, Long> {

	@Query(nativeQuery = true, value = """
			WITH filtered AS (
		    SELECT report_time_utc, wind_speed_kt, wind_dir_deg, wind_dir_type
		    FROM tbsst_metar_report
		    WHERE station_icao = :icao
		      AND report_time_utc >= :fromInclusive
		      AND report_time_utc <  :toExclusive
			),
			fixed_only AS (
			  SELECT *
			  FROM filtered
			  WHERE wind_dir_type = 'FIXED'
			    AND wind_dir_deg IS NOT NULL
			),
			binned AS (
			  SELECT
			    YEAR(report_time_utc) AS y,
			    MONTH(report_time_utc) AS m,
			    FLOOR(MOD(wind_dir_deg + 11.25, 360) / 22.5) AS dirOrder,
			    CASE FLOOR(MOD(wind_dir_deg + 11.25, 360) / 22.5)
			      WHEN  0 THEN 'N'   WHEN  1 THEN 'NNE' WHEN  2 THEN 'NE'  WHEN  3 THEN 'ENE'
			      WHEN  4 THEN 'E'   WHEN  5 THEN 'ESE' WHEN  6 THEN 'SE'  WHEN  7 THEN 'SSE'
			      WHEN  8 THEN 'S'   WHEN  9 THEN 'SSW' WHEN 10 THEN 'SW'  WHEN 11 THEN 'WSW'
			      WHEN 12 THEN 'W'   WHEN 13 THEN 'WNW' WHEN 14 THEN 'NW'  WHEN 15 THEN 'NNW'
			    END AS dirLabel,
			    CASE
			      WHEN wind_speed_kt < 1                          THEN 0
			      WHEN wind_speed_kt >= 1  AND wind_speed_kt < 5  THEN 1
			      WHEN wind_speed_kt >= 5  AND wind_speed_kt < 10 THEN 2
			      WHEN wind_speed_kt >= 10 AND wind_speed_kt < 15 THEN 3
			      WHEN wind_speed_kt >= 15 AND wind_speed_kt < 20 THEN 4
			      WHEN wind_speed_kt >= 20 AND wind_speed_kt < 25 THEN 5
			      ELSE 6
			    END AS speedOrder,
			    CASE
			      WHEN wind_speed_kt < 1                          THEN 'CALM'
			      WHEN wind_speed_kt >= 1  AND wind_speed_kt < 5  THEN '1-5KT'
			      WHEN wind_speed_kt >= 5  AND wind_speed_kt < 10 THEN '5-10KT'
			      WHEN wind_speed_kt >= 10 AND wind_speed_kt < 15 THEN '10-15KT'
			      WHEN wind_speed_kt >= 15 AND wind_speed_kt < 20 THEN '15-20KT'
			      WHEN wind_speed_kt >= 20 AND wind_speed_kt < 25 THEN '20-25KT'
			      ELSE '25+KT'
			    END AS speedLabel
			  FROM fixed_only
			)
			SELECT
			  y,
			  m,
			  dirOrder, dirLabel,
		    speedOrder, speedLabel,
			  COUNT(*) AS freq
			FROM binned
			GROUP BY y, m, dirOrder, dirLabel, speedOrder, speedLabel
			ORDER BY y, m, dirOrder, speedOrder;
		""")
	List<WindRoseRow> aggregateDefaultByMonth(@Param("icao") String icao,
	                                          @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                          @Param("toExclusive") ZonedDateTime toExclusive);


	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), COUNT(m)
			)
		   FROM MetarData m
		   WHERE m.stationIcao = :icao
		     AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
		     AND (m.windDirectionType = :variable OR m.windDirectionType IS NULL)
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countVariableByMonth(@Param("icao") String icao,
	                                                @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                @Param("toExclusive") ZonedDateTime toExclusive,
	                                                @Param("variable") WindDirectionType variable);

}
