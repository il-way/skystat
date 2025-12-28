package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.HourlyCountSummary;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountSummary;
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
	List<MonthlyCountQueryDto> countPhenomenaExactMatchDaysByMonth(@Param("icao") String icao,
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
		       SELECT 1 FROM WeatherData w JOIN w.phenomena wp
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0 AND wp.phenomenon IN :phenomena
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countPhenomenaExistAnyDaysByMonth(@Param("icao") String icao,
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
	List<HourlyCountQueryDto> countPhenomenaExactMatchDaysByMonthHour(@Param("icao") String icao,
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
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0 AND wp.phenomenon IN :phenomena
				)
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countPhenomenaExistAnyDaysByMonthHour(@Param("icao") String icao,
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
	List<MonthlyCountQueryDto> countDescriptorExactMatchDaysByMonth(@Param("icao") String icao,
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
		       SELECT 1 FROM WeatherData w JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0 AND wd.descriptor IN :descriptors 
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countDescriptorExistAnyDaysByMonth(@Param("icao") String icao,
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
	List<HourlyCountQueryDto> countDescriptorExactMatchDaysByMonthHour(@Param("icao") String icao,
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
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0 AND wd.descriptor IN :descriptors
				)
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countDescriptorExistAnyDaysByMonthHour(@Param("icao") String icao,
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
	List<MonthlyCountQueryDto> countDescriptorAndPhenomenaExactMatchDaysByMonth(@Param("icao") String icao,
	                                                                            @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                                            @Param("toExclusive") ZonedDateTime toExclusive,
	                                                                            @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                                            @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                                            @Param("descriptorsCount") int descriptorsCount,
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
		       SELECT 1 FROM WeatherData w JOIN w.phenomena wp JOIN w.descriptors wd
		       WHERE w.metar = m AND LOCATE(:rawCode, w.rawCode) > 0
		          AND wp.phenomenon IN :phenomena
		          AND wd.descriptor IN :descriptors
		     )
		   GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
		   ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
		""")
	List<MonthlyCountQueryDto> countDescriptorAndPhenomenaExsitAnyDaysByMonth(@Param("icao") String icao,
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
	List<HourlyCountQueryDto> countDescriptorAndPhenomenaExactMatchDaysByMonthHour(@Param("icao") String icao,
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
		          AND wp.phenomenon IN :phenomena
		          AND wd.descriptor IN :descriptors
		     )
			GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
			ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		""")
	List<HourlyCountQueryDto> countDescriptorAndPhenomenaExistAnyDaysByMonthHour(@Param("icao") String icao,
	                                                                             @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                                             @Param("toExclusive") ZonedDateTime toExclusive,
	                                                                             @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                                             @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                                             @Param("descriptorsCount") int descriptorsCount,
	                                                                             @Param("phenomenaCount") int phenomenaCount,
	                                                                             @Param("rawCode") String rawCode);


	// ==================================================================================
	// [Native Query] Descriptor 단독 조회 (Phenomenon 제외)
	// - Report(부모) -> Weather -> Descriptor 조인
	// - FORCE INDEX (idx_station_rpt): 부모 테이블 풀 스캔 방지 (필수)
	// - USE INDEX (idx_metar_code, idx_weather_descriptor): 자식 테이블 커버링 강제
	// ==================================================================================

	/**
	 * 1. [Monthly] Descriptor Exact Match (월별 / 정확히 일치)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		          AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :#{#descriptors.![name()]} THEN wd.descriptor END) = :descriptorsCount
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		""", nativeQuery = true)
	List<MonthlyCountSummary> countDescriptorExactMatchDaysByMonthNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("rawCode") String rawCode);


	/**
	 * 2. [Monthly] Descriptor Exist Any (월별 / 하나라도 포함)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		""", nativeQuery = true)
	List<MonthlyCountSummary> countDescriptorExistAnyDaysByMonthNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("rawCode") String rawCode);


	/**
	 * 3. [Hourly] Descriptor Exact Match (시간별 / 정확히 일치)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     HOUR(m.report_time_utc) as h,
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		          AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :#{#descriptors.![name()]} THEN wd.descriptor END) = :descriptorsCount
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		""", nativeQuery = true)
	List<HourlyCountSummary> countDescriptorExactMatchDaysByMonthHourNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("rawCode") String rawCode);


	/**
	 * 4. [Hourly] Descriptor Exist Any (시간별 / 하나라도 포함)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     HOUR(m.report_time_utc) as h,
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		""", nativeQuery = true)
	List<HourlyCountSummary> countDescriptorExistAnyDaysByMonthHourNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("rawCode") String rawCode);


	// ==================================================================================
	// [Native Query] Descriptor + Phenomenon 복합 조회
	// - 4개 테이블 조인: Report -> Weather -> Descriptor, Phenomenon
	// - 모든 테이블에 인덱스 힌트 적용하여 풀 스캔 원천 봉쇄
	// ==================================================================================

	/**
	 * 1. [Monthly] Descriptor + Phenomenon (Exact Match)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_phenomenon wp USE INDEX (idx_weather_phenomenon)
		         ON w.id = wp.weather_id
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		
		         -- [그룹핑 & 카운팅으로 정확히 일치하는지 검증]
		       GROUP BY w.id
		       HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		          AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :#{#phenomena.![name()]} THEN wp.phenomenon END) = :phenomenaCount
		          AND COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		          AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :#{#descriptors.![name()]} THEN wd.descriptor END) = :descriptorsCount
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		""", nativeQuery = true)
	List<MonthlyCountSummary> countDescriptorAndPhenomenaExactMatchDaysByMonthNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("phenomena") List<WeatherPhenomenon> phenomena,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("phenomenaCount") int phenomenaCount,
		@Param("rawCode") String rawCode);


	/**
	 * 2. [Monthly] Descriptor + Phenomenon (Exist Any)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_phenomenon wp USE INDEX (idx_weather_phenomenon)
		         ON w.id = wp.weather_id
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		
		         -- [단순 포함 여부 체크]
		         AND wp.phenomenon IN :#{#phenomena.![name()]}
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc)
		""", nativeQuery = true)
	List<MonthlyCountSummary> countDescriptorAndPhenomenaExsitAnyDaysByMonthNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("phenomena") List<WeatherPhenomenon> phenomena,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("phenomenaCount") int phenomenaCount,
		@Param("rawCode") String rawCode);


	/**
	 * 3. [Hourly] Descriptor + Phenomenon (Exact Match)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     HOUR(m.report_time_utc) as h,
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_phenomenon wp USE INDEX (idx_weather_phenomenon)
		         ON w.id = wp.weather_id
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         GROUP BY w.id
		         HAVING COUNT(DISTINCT wp.phenomenon) = :phenomenaCount
		           AND COUNT(DISTINCT CASE WHEN wp.phenomenon IN :#{#phenomena.![name()]} THEN wp.phenomenon END) = :phenomenaCount
		           AND COUNT(DISTINCT wd.descriptor) = :descriptorsCount
		           AND COUNT(DISTINCT CASE WHEN wd.descriptor IN :#{#descriptors.![name()]} THEN wd.descriptor END) = :descriptorsCount
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		""", nativeQuery = true)
	List<HourlyCountSummary> countDescriptorAndPhenomenaExactMatchDaysByMonthHourNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("phenomena") List<WeatherPhenomenon> phenomena,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("phenomenaCount") int phenomenaCount,
		@Param("rawCode") String rawCode);


	/**
	 * 4. [Hourly] Descriptor + Phenomenon (Exist Any)
	 */
	@Query(value = """
		 SELECT 
		     YEAR(m.report_time_utc) as y, 
		     MONTH(m.report_time_utc) as m, 
		     HOUR(m.report_time_utc) as h,
		     COUNT(DISTINCT DAY(m.report_time_utc)) as c
		 FROM tbsst_metar_report m FORCE INDEX (idx_station_rpt)
		 WHERE m.station_icao = :icao
		   AND m.report_time_utc >= :fromInclusive 
		   AND m.report_time_utc < :toExclusive
		   AND EXISTS (
		       SELECT 1 
		       FROM tbsst_metar_weather w USE INDEX (idx_metar_code)
		       JOIN tbsst_metar_weather_phenomenon wp USE INDEX (idx_weather_phenomenon)
		         ON w.id = wp.weather_id
		       JOIN tbsst_metar_weather_descriptor wd USE INDEX (idx_weather_descriptor)
		         ON w.id = wd.weather_id
		       WHERE w.metar_id = m.id 
		         AND LOCATE(:rawCode, w.raw_code) > 0 
		         AND wp.phenomenon IN :#{#phenomena.![name()]}
		         AND wd.descriptor IN :#{#descriptors.![name()]}
		     )
		 GROUP BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		 ORDER BY YEAR(m.report_time_utc), MONTH(m.report_time_utc), HOUR(m.report_time_utc)
		""", nativeQuery = true)
	List<HourlyCountSummary> countDescriptorAndPhenomenaExistAnyDaysByMonthHourNative(
		@Param("icao") String icao,
		@Param("fromInclusive") ZonedDateTime fromInclusive,
		@Param("toExclusive") ZonedDateTime toExclusive,
		@Param("descriptors") List<WeatherDescriptor> descriptors,
		@Param("phenomena") List<WeatherPhenomenon> phenomena,
		@Param("descriptorsCount") int descriptorsCount,
		@Param("phenomenaCount") int phenomenaCount,
		@Param("rawCode") String rawCode);


}
