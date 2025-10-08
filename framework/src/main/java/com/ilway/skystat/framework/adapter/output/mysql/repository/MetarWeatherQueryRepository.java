package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarWeatherQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
			)
		   FROM MetarData m
		   WHERE m.stationIcao = :icao
		     AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
		     AND EXISTS (
		       SELECT 1 FROM WeatherData w JOIN w.phenomena wp
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		          AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :phenomena THEN wp.phenomenon END) = :phenomenaCount
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countPhenomenaDaysByMonth(@Param("icao") String icao,
	                                                     @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                     @Param("toExclusive") ZonedDateTime toExclusive,
	                                                     @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                     @Param("phenomenaCount") int phenomenaCount,
	                                                     @Param("rawCode") String rawCode);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
				COUNT(DISTINCT DAY(m.reportTime))
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
				AND EXISTS (
					SELECT 1 FROM WeatherData w JOIN w.phenomena wp
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		          AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :phenomena THEN wp.phenomenon END) = :phenomenaCount
				)
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countPhenomenaDaysByMonthHour(@Param("icao") String icao,
	                                                        @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                        @Param("toExclusive") ZonedDateTime toExclusive,
	                                                        @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                        @Param("phenomenaCount") int phenomenaCount,
	                                                        @Param("rawCode") String rawCode);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
			)
		   FROM MetarData m
		   WHERE m.stationIcao = :icao
		     AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
		     AND EXISTS (
		       SELECT 1 FROM WeatherData w JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		          AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :descriptors THEN wd.descriptor END) = :descriptorsCount
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countDescriptorDaysByMonth(@Param("icao") String icao,
	                                                      @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                      @Param("toExclusive") ZonedDateTime toExclusive,
	                                                      @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                      @Param("descriptorsCount") int descriptorsCount,
	                                                      @Param("rawCode") String rawCode);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
				COUNT(DISTINCT DAY(m.reportTime))
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
				AND EXISTS (
					SELECT 1 FROM WeatherData w JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		          AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :descriptors THEN wd.descriptor END) = :descriptorsCount
				)
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countDescriptorDaysByMonthHour(@Param("icao") String icao,
	                                                         @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                         @Param("toExclusive") ZonedDateTime toExclusive,
	                                                         @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                         @Param("descriptorsCount") int descriptorsCount,
	                                                         @Param("rawCode") String rawCode);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
			)
		   FROM MetarData m
		   WHERE m.stationIcao = :icao
		     AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
		     AND EXISTS (
		       SELECT 1 FROM WeatherData w JOIN w.phenomena wp JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		         AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :phenomena THEN wp.phenomenon END) = :phenomenaCount
		         AND COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		         AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :descriptors THEN wd.descriptor END) = :descriptorsCount
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countDescriptorAndPhenomenaDaysByMonth(@Param("icao") String icao,
	                                                                  @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                                  @Param("toExclusive") ZonedDateTime toExclusive,
	                                                                  @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                                  @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                                  @Param("descriptorsCount") int descriptorsCount,
	                                                                  @Param("phenomenaCount") int phenomenaCount,
	                                                                  @Param("rawCode") String rawCode);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto(
				YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
				COUNT(DISTINCT DAY(m.reportTime))
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
				AND EXISTS (
		       SELECT 1 FROM WeatherData w JOIN w.phenomena wp JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		         AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :phenomena THEN wp.phenomenon END) = :phenomenaCount
		         AND COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		         AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :descriptors THEN wd.descriptor END) = :descriptorsCount
		     )
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countDescriptorAndPhenomenaDaysByMonthHour(@Param("icao") String icao,
	                                                                     @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                                     @Param("toExclusive") ZonedDateTime toExclusive,
	                                                                     @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                                     @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                                     @Param("descriptorsCount") int descriptorsCount,
	                                                                     @Param("phenomenaCount") int phenomenaCount,
	                                                                     @Param("rawCode") String rawCode);

}
