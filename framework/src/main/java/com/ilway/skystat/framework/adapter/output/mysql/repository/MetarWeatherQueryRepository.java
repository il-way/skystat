package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.domain.vo.weather.type.CloudType;
import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetarWeatherQueryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
    FROM MetarData m
    WHERE UPPER(m.stationIcao) = UPPER(:icao)
      AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
      AND EXISTS (
        SELECT 1 FROM WeatherData w JOIN w.phenomena ph
        WHERE w.metar = m AND ph.phenomenon IN :phenomena
        GROUP BY w.metar
        HAVING COUNT(DISTINCT ph.phenomenon) = :phenomenaCount
      )
    GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
    ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countPhenomenaDaysByMonth(@Param("icao") String icao,
	                                                @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                @Param("toExclusive") ZonedDateTime toExclusive,
	                                                @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                @Param("phenomenaCount") int phenomenaCount);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND EXISTS (
				SELECT 1 FROM WeatherData w JOIN w.phenomena ph
        WHERE w.metar = m AND ph.phenomenon IN :phenomena
        GROUP BY w.metar
        HAVING COUNT(DISTINCT ph.phenomenon) = :phenomenaCount
			)
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countPhenomenaDaysByMonthHour(@Param("icao") String icao,
	                                                          @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                          @Param("toExclusive") ZonedDateTime toExclusive,
	                                                          @Param("phenomena") List<WeatherPhenomenon> phenomena,
	                                                          @Param("phenomenaCount") int phenomenaCount);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
		)
    FROM MetarData m
    WHERE UPPER(m.stationIcao) = UPPER(:icao)
      AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
      AND EXISTS (
        SELECT 1 FROM WeatherData w JOIN w.descriptors wd
        WHERE w.metar = m AND wd.descriptor IN :descriptors
        GROUP BY w.metar
        HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
      )
    GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
    ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
	""")
	List<MonthlyDayCount> countDescriptorDaysByMonth(@Param("icao") String icao,
	                                                 @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                 @Param("toExclusive") ZonedDateTime toExclusive,
	                                                 @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                 @Param("phenomenaCount") int descriptorsCount);

	@Query("""
		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
			COUNT(DISTINCT DAY(m.reportTime))
		)
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
			AND EXISTS (
				SELECT 1 FROM WeatherData w JOIN w.descriptors wd
        WHERE w.metar = m AND wd.descriptor IN :descriptors
        GROUP BY w.metar
        HAVING COUNT(DISTINCT wd.descriptor) = :descriptorsCount
			)
		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
	""")
	List<MonthlyHourlyDayCount> countDescriptorDaysByMonthHour(@Param("icao") String icao,
	                                                           @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                                           @Param("toExclusive") ZonedDateTime toExclusive,
	                                                           @Param("descriptors") List<WeatherDescriptor> descriptors,
	                                                           @Param("phenomenaCount") int descriptorsCount);

//	@Query("""
//		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyDayCount(
//			YEAR(m.reportTime), MONTH(m.reportTime), COUNT(DISTINCT DAY(m.reportTime))
//		)
//    FROM MetarData m
//    WHERE UPPER(m.stationIcao) = UPPER(:icao)
//      AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
//      AND EXISTS (
//        SELECT 1 FROM WeatherData w JOIN w.phenomena wp JOIN w.descriptors wd
//        WHERE w.metar = m
//          AND wd.descriptor = :descriptor
//          AND wp.phenomena = :phenomena
//      )
//    GROUP BY YEAR(m.reportTime), MONTH(m.reportTime)
//    ORDER BY YEAR(m.reportTime), MONTH(m.reportTime)
//	""")
//	List<MonthlyDayCount> countDescriptorAndPhenomenaDaysByMonth(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, WeatherDescriptor descriptor, WeatherPhenomenon phenomenon);
//
//	@Query("""
//		SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyHourlyDayCount(
//			YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime),
//			COUNT(DISTINCT DAY(m.reportTime))
//		)
//		FROM MetarData m
//		WHERE UPPER(m.stationIcao) = UPPER(:icao)
//			AND m.reportTime >= :fromInclusive AND m.reportTime < :toExclusive
//			AND EXISTS (
//        SELECT 1 FROM WeatherData w JOIN w.phenomena wp JOIN w.descriptors wd
//        WHERE w.metar = m
//          AND wd.descriptor = :descriptor
//          AND wp.phenomena = :phenomena
//      )
//		GROUP BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
//		ORDER BY YEAR(m.reportTime), MONTH(m.reportTime), HOUR(m.reportTime)
//	""")
//	List<MonthlyHourlyDayCount> countDescriptorAndPhenomenaDaysByMonthHour(String icao, ZonedDateTime fromInclusive, ZonedDateTime toExclusive, WeatherDescriptor descriptor);

}
