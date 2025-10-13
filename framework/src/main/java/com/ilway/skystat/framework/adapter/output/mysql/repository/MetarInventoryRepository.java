package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.CoverageQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface MetarInventoryRepository extends JpaRepository<MetarData, Long> {

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.CoverageQueryDto(
				MIN(m.reportTime), MAX(m.reportTime), COUNT(m)
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
		""")
	CoverageQueryDto findDatasetCoverage(@Param("icao") String icao);

	@Query("""
			SELECT new com.ilway.skystat.framework.adapter.output.mysql.repository.dto.CoverageQueryDto(
				MIN(m.reportTime), MAX(m.reportTime), COUNT(m)
			)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.reportTime >= :fromInclusive
				AND m.reportTime < :toExclusive
		""")
	CoverageQueryDto findPeriodCoverage(@Param("icao") String icao,
	                                    @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                    @Param("toExclusive") ZonedDateTime toExclusive
	                                    );

}
